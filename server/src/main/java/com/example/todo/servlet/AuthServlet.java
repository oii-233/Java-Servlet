package com.example.todo.servlet;

import com.example.todo.model.User;
import com.example.todo.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * AuthServlet is the HTTP controller for registration and login.
 * It delegates business decisions to UserService.
 */
public class AuthServlet extends HttpServlet {
    private final transient Gson gson = new Gson();
    private final transient UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if (path == null) {
            path = "";
        }
        // Normalize trailing slash (e.g., "/register/" -> "/register")
        if (!path.isEmpty() && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        try (PrintWriter out = resp.getWriter()) {
            if (path.equals("/register")) {
                JsonObject body = readJson(req);
                String username = getAsString(body, "username");
                String password = getAsString(body, "password");
                try {
                    User user = userService.register(username, password);
                    HttpSession session = req.getSession(true);
                    session.setAttribute("userId", user.getId());
                    session.setMaxInactiveInterval(60 * 60 * 24); // 1 day
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    JsonObject res = new JsonObject();
                    res.addProperty("id", user.getId());
                    res.addProperty("username", user.getUsername());
                    out.println(gson.toJson(res));
                    out.flush();
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JsonObject err = new JsonObject();
                    err.addProperty("error", e.getMessage());
                    out.println(gson.toJson(err));
                    out.flush();
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject err = new JsonObject();
                    err.addProperty("error", "Database error: " + e.getMessage());
                    out.println(gson.toJson(err));
                    out.flush();
                }
            } else if (path.equals("/login")) {
                JsonObject body = readJson(req);
                String username = getAsString(body, "username");
                String password = getAsString(body, "password");
                try {
                    User user = userService.login(username, password);
                    if (user == null) {
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        JsonObject err = new JsonObject();
                        err.addProperty("error", "Invalid credentials");
                        out.println(gson.toJson(err));
                        out.flush();
                        return;
                    }
                    // Issue JWT token for client usage
                    String token = com.example.todo.util.JWTUtil.issueToken(user.getId(), user.getUsername());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObject res = new JsonObject();
                    res.addProperty("id", user.getId());
                    res.addProperty("username", user.getUsername());
                    res.addProperty("token", token);
                    res.addProperty("message", "user successfully logged in");
                    out.println(gson.toJson(res));
                    out.flush();
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject err = new JsonObject();
                    err.addProperty("error", "Database error: " + e.getMessage());
                    out.println(gson.toJson(err));
                    out.flush();
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println(gson.toJson(error("Not found")));
                out.flush();
            }
        }
    }

    private JsonObject readJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return gson.fromJson(sb.toString(), JsonObject.class);
    }

    private String getAsString(JsonObject obj, String key) {
        return obj != null && obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    private JsonObject error(String message) {
        JsonObject err = new JsonObject();
        err.addProperty("error", message);
        return err;
    }
}
