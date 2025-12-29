package com.example.todo.filter;

import com.example.todo.util.EnvUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * CORS filter enabling credentials and restricting origin.
 * Reads FRONTEND_ORIGIN from env; defaults to localhost:5173.
 */
public class CorsFilter implements Filter {
    private Set<String> allowedOrigins;

    @Override
    public void init(FilterConfig filterConfig) {
        String envOrigin = EnvUtil.get("FRONTEND_ORIGIN");
        if (envOrigin == null || envOrigin.isEmpty()) {
            envOrigin = "http://localhost:5173";
        }
        allowedOrigins = new HashSet<>(Arrays.asList(envOrigin, "http://127.0.0.1:5173"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        if (origin != null && allowedOrigins.contains(origin)) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
            resp.setHeader("Vary", "Origin");
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        }

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
