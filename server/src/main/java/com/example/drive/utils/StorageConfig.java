package com.example.drive.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class StorageConfig {
    private static String basePath;

    private StorageConfig() {
    }

    public static void setBasePath(String path) {
        basePath = path;
    }

    public static Path getUploadsPath() {
        return Paths.get(basePath, "files");
    }

    public static Path getDbPath() {
        return Paths.get(basePath, "db");
    }
}
