package com.example.drive.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtil() {
    }

    public static void write(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(data));
    }

    public static void error(HttpServletResponse resp, int status, String message) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("error", message);
        write(resp, status, body);
    }
}
