package br.com.fernandouchoa.qa.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class EnvironmentManager {

    private static final Properties properties = new Properties();
    private static final String DEFAULT_ENV = "dev";

    static {
        String environment = System.getProperty("env", DEFAULT_ENV);
        String filePath = String.format("environments/%s.properties", environment);

        try (InputStream inputStream = EnvironmentManager.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException("Arquivo de ambiente não encontrado: " + filePath);
            }

            properties.load(inputStream);

        } catch (IOException exception) {
            throw new RuntimeException("Erro ao carregar arquivo de ambiente: " + filePath, exception);
        }
    }

    private EnvironmentManager() {
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBrowser() {
        return properties.getProperty("browser", "chromium");
    }

    public static boolean isHeadless() {

        boolean isCi =
                "true".equalsIgnoreCase(System.getenv("CI"))
                        || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));

        if (isCi) {
            return true;
        }

        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("timeout", "30000"));
    }
}
