package br.com.fernandouchoa.qa.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ScreenshotType;

import io.qameta.allure.Allure;

public final class AllureUtils {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AllureUtils.class);

    private static final Path ALLURE_RESULTS_PATH =
            Paths.get("target", "allure-results");

    private static final Path ALLURE_REPORT_PATH =
            Paths.get("target", "allure-report");

    private AllureUtils() {
    }

    public static void attachScreenshot(Page page, String name) {
        if (page == null || page.isClosed()) {
            return;
        }

        try {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setType(ScreenshotType.PNG)
                            .setFullPage(true)
                            .setTimeout(30000)
            );

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (Exception exception) {
            LOGGER.warn("Não foi possível capturar o screenshot.", exception);
            attachText("Falha ao capturar screenshot", exception.toString());
        }
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(
                name,
                "text/plain",
                content == null ? "Sem detalhes disponíveis." : content
        );
    }

    public static void attachFile(
            String name,
            Path filePath,
            String type,
            String extension) {

        if (!isValidFile(filePath)) {
            return;
        }

        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Allure.addAttachment(
                    name,
                    type,
                    inputStream,
                    extension
            );
        } catch (IOException exception) {
            LOGGER.warn("Não foi possível anexar o arquivo {}.", filePath, exception);
        }
    }

    public static void cleanResults() {
        delete(ALLURE_RESULTS_PATH);
        delete(ALLURE_REPORT_PATH);
    }

    private static boolean isValidFile(Path filePath) {
        if (filePath == null) {
            return false;
        }

        try {
            return Files.exists(filePath)
                    && Files.isRegularFile(filePath)
                    && Files.size(filePath) > 0;
        } catch (IOException exception) {
            LOGGER.warn("Não foi possível validar o arquivo {}.", filePath, exception);
            return false;
        }
    }

    private static void delete(Path path) {
        if (!Files.exists(path)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(AllureUtils::deleteFile);
        } catch (IOException exception) {
            LOGGER.warn("Não foi possível limpar o diretório {}.", path, exception);
        }
    }

    private static void deleteFile(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException exception) {
            LOGGER.warn("Não foi possível excluir o arquivo {}.", file, exception);
        }
    }
}
