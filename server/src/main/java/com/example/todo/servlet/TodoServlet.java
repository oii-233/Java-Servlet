package com.example.todo.servlet;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.List;

/**
 * TodoServlet handles CRUD for todos under /api/todos.
 * Requires authenticated session (HttpSession with userId).
 */
public class TodoServlet extends HttpServlet {
    private final transient Gson gson = new Gson();
    private final transient TodoService todoService = new TodoService();

    private Integer requireUserId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Prefer userId set by JWTAuthFilter
        Object uidAttr = req.getAttribute("userId");
        if (uidAttr instanceof Integer) {
            return (Integer) uidAttr;
        }

        // Fallback to session for legacy clients
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return (Integer) session.getAttribute("userId");
        }

        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(error("Authentication required")));
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer userId = requireUserId(req, resp);
        if (userId == null)
            return;
        try (PrintWriter out = resp.getWriter()) {
            try {
                List<Todo> todos = todoService.listTodos(userId);
                JsonArray arr = new JsonArray();
                for (Todo t : todos) {
                    arr.add(toJson(t));
                }
                out.println(gson.toJson(arr));
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson(error("Database error: " + e.getMessage())));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer userId = requireUserId(req, resp);
        if (userId == null)
            return;
        JsonObject body = readJson(req);
        String title = getAsString(body, "title");
        try (PrintWriter out = resp.getWriter()) {
            try {
                Todo created = todoService.createTodo(title, userId);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.println(gson.toJson(toJson(created)));
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson(error(e.getMessage())));
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson(error("Database error: " + e.getMessage())));
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer userId = requireUserId(req, resp);
        if (userId == null)
            return;
        String path = req.getPathInfo();
        Integer id = pathId(path);
        JsonObject body = readJson(req);
        boolean completed = body != null && body.has("completed") && body.get("completed").getAsBoolean();
        try (PrintWriter out = resp.getWriter()) {
            if (id == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson(error("Missing todo id in path: /api/todos/{id}")));
                return;
            }
            try {
                boolean ok = todoService.setCompleted(id, completed, userId);
                if (!ok) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println(gson.toJson(error("Todo not found or not yours")));
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObject res = new JsonObject();
                res.addProperty("success", true);
                out.println(gson.toJson(res));
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson(error("Database error: " + e.getMessage())));
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer userId = requireUserId(req, resp);
        if (userId == null)
            return;
        String path = req.getPathInfo();
        Integer id = pathId(path);
        try (PrintWriter out = resp.getWriter()) {
            if (id == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(gson.toJson(error("Missing todo id in path: /api/todos/{id}")));
                return;
            }
            try {
                boolean ok = todoService.delete(id, userId);
                if (!ok) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println(gson.toJson(error("Todo not found or not yours")));
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson(error("Database error: " + e.getMessage())));
            }
        }
    }

    private Integer pathId(String path) {
        if (path == null || path.isEmpty() || path.equals("/"))
            return null;
        String[] parts = path.split("/");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private JsonObject toJson(Todo t) {
        JsonObject o = new JsonObject();
        o.addProperty("id", t.getId());
        o.addProperty("title", t.getTitle());
        o.addProperty("completed", t.isCompleted());
        o.addProperty("userId", t.getUserId());
        return o;
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
