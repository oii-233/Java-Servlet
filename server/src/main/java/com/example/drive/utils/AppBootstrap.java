package com.example.drive.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.nio.file.Files;
import java.nio.file.Path;

@WebListener
public class AppBootstrap implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String storageBase = sce.getServletContext().getInitParameter("storage.basePath");
        if (storageBase == null || storageBase.isBlank()) {
            storageBase = "/tmp/mini-drive/uploads";
        }
        StorageConfig.setBasePath(storageBase);
        createDirectories(StorageConfig.getUploadsPath());
        createDirectories(StorageConfig.getDbPath());
        Db.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op for now
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create directory: " + path, e);
        }
    }
}
