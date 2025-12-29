package com.example.todo.util;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * EnvUtil loads configuration from environment variables or a local .env file.
 * Priority: System environment variables > .env file (if present).
 */
public final class EnvUtil {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private EnvUtil() {
    }

    public static String get(String key) {
        String val = System.getenv(key);
        if (val == null || val.isEmpty()) {
            val = dotenv.get(key);
        }
        return val;
    }

    public static String getRequired(String key) {
        String v = get(key);
        if (v == null || v.isEmpty()) {
            throw new IllegalStateException("Missing required env var: " + key);
        }
        return v;
    }
}
