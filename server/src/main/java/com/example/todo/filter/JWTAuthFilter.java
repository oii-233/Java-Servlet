package com.example.todo.filter;

import com.example.todo.util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWTAuthFilter validates Bearer tokens and sets request attribute 'userId'.
 */
public class JWTAuthFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(resp, "Missing Authorization header");
            return;
        }
        String token = auth.substring("Bearer ".length());
        try {
            DecodedJWT jwt = JWTUtil.verify(token);
            Integer uid = jwt.getClaim("uid").asInt();
            if (uid == null) {
                unauthorized(resp, "Invalid token claims");
                return;
            }
            req.setAttribute("userId", uid);
            chain.doFilter(request, response);
        } catch (Exception e) {
            unauthorized(resp, "Invalid or expired token");
        }
    }

    private void unauthorized(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonObject err = new JsonObject();
        err.addProperty("error", message);
        try (PrintWriter out = resp.getWriter()) {
            out.println(gson.toJson(err));
        }
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}
