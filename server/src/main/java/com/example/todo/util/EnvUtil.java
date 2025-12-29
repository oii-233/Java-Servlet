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
        // 1) OS env var
        String val = System.getenv(key);
        // 2) Java system property (e.g., -DDB_URL=...)
        if (isBlank(val)) {
            val = System.getProperty(key);
        }
        // 3) .env file (if present)
        if (isBlank(val)) {
            val = dotenv.get(key);
        }
        return sanitize(val);
    }

    public static String getRequired(String key) {
        String v = get(key);
        if (isBlank(v)) {
            throw new IllegalStateException("Missing required env var: " + key);
        }
        return v;
    }

    /**
     * Compose a JDBC URL from PG* variables if DB_URL is not provided.
     * Supported PG vars: PGHOST, PGPORT (default 5432), PGDATABASE, PGSSLMODE (default require), PGCHANNELBINDING (optional).
     */
    public static String getJdbcUrl() {
        String explicit = get("DB_URL");
        if (!isBlank(explicit)) {
            return explicit;
        }
        String host = get("PGHOST");
        String db = get("PGDATABASE");
        String port = get("PGPORT");
        if (isBlank(port)) port = "5432";
        String sslmode = get("PGSSLMODE");
        if (isBlank(sslmode)) sslmode = "require";
        String channelBinding = get("PGCHANNELBINDING");

        if (isBlank(host) || isBlank(db)) {
            throw new IllegalStateException("Missing required env vars to compose JDBC URL (need PGHOST and PGDATABASE)");
        }
        StringBuilder url = new StringBuilder("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(db);
        StringBuilder query = new StringBuilder("sslmode=").append(sslmode);
        if (!isBlank(channelBinding)) {
            query.append("&channel_binding=").append(channelBinding);
        }
        url.append("?").append(query);
        return url.toString();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String sanitize(String v) {
        if (v == null)
            return null;
        String s = v.trim();
        // Strip surrounding single or double quotes if present
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }
}
