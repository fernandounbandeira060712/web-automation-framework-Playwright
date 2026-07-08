package br.com.fernandouchoa.qa.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centraliza as configurações de ambiente do framework.
 *
 * As propriedades são carregadas a partir do arquivo correspondente ao ambiente
 * informado pela system property "env". Caso nenhum ambiente seja informado,
 * o ambiente "dev" será utilizado.
 */
public final class EnvironmentManager {

    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_ENV = "dev";

    static {
        String filePath =
                String.format("environments/%s.properties", getEnvironment());

        try (InputStream inputStream = EnvironmentManager.class
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "Arquivo de ambiente não encontrado: " + filePath);
            }

            PROPERTIES.load(inputStream);

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Erro ao carregar arquivo de ambiente: " + filePath,
                    exception);
        }
    }

    private EnvironmentManager() {
    }

    public static String getEnvironment() {
        return System.getProperty("env", DEFAULT_ENV);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getBrowser() {
        return get("browser", "chromium");
    }

    public static boolean isHeadless() {

        boolean isCi =
                "true".equalsIgnoreCase(System.getenv("CI"))
                        || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));

        if (isCi) {
            return true;
        }

        return Boolean.parseBoolean(get("headless", "false"));
    }

    public static int getTimeout() {
        return getInt("timeout", 30000);
    }

    private static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static int getInt(String key, int defaultValue) {
        return Integer.parseInt(
                get(key, String.valueOf(defaultValue))
        );
    }
}