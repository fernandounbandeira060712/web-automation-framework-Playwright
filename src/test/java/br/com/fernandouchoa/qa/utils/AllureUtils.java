package br.com.fernandouchoa.qa.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;

import io.qameta.allure.Allure;

public final class AllureUtils {

    private AllureUtils() {
    }

    public static void attachScreenshot(Page page, String name) {

        byte[] screenshot =
                page.screenshot(
                        new Page.ScreenshotOptions()
                                .setType(ScreenshotType.PNG)
                                .setFullPage(true)
                );

        Allure.addAttachment(
                name,
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }

    public static void cleanResults() {

        try {
            Path results = Paths.get("target", "allure-results");
            Path report = Paths.get("target", "allure-report");

            delete(results);
            delete(report);

        } catch (Exception ignored) {
        }
    }

    private static void delete(Path path) throws IOException {

        if (!Files.exists(path)) {
            return;
        }

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(file -> {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException ignored) {
                    }
                });
    }
    
    public static void attachFile(String name, Path filePath, String type) {

        try {
            if (Files.exists(filePath) && Files.size(filePath) > 0) {
                Allure.addAttachment(
                        name,
                        type,
                        Files.newInputStream(filePath),
                        ".zip"
                );
            }
        } catch (Exception ignored) {
        }
    }
}