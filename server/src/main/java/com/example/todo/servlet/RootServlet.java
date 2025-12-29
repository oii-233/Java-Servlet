package com.example.todo.servlet;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * RootServlet returns a simple JSON when hitting the root path '/'.
 */
public class RootServlet extends HttpServlet {
    private final transient Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JsonObject json = new JsonObject();
        json.addProperty("message", "server is running");
        json.addProperty("app", "todo-servlet");
        json.addProperty("version", "1.0.0");
        try (PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(json));
        }
    }
}
