package br.com.fernandouchoa.qa.core.extensions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fernandouchoa.qa.core.driver.DriverManager;
import br.com.fernandouchoa.qa.utils.AllureUtils;

/**
 * Gerencia recursos que devem existir uma única vez por suíte de testes.
 *
 * <p>A extensão limpa os resultados antigos do Allure de forma segura para
 * execução paralela, captura screenshot somente quando o teste falha e pode
 * abrir o relatório local ao final da suíte quando a propriedade
 * {@code -Dallure.open.report=true} for informada.</p>
 */
public final class AllureReportExtension
        implements BeforeAllCallback, AfterTestExecutionCallback {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AllureReportExtension.class);

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(AllureReportExtension.class);

    private static final String SUITE_RESOURCE_KEY = "allure-suite-resource";

    @Override
    public void beforeAll(ExtensionContext context) {
        context.getRoot()
                .getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        SUITE_RESOURCE_KEY,
                        ignored -> new SuiteResource(),
                        SuiteResource.class
                );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        context.getExecutionException().ifPresent(exception -> {
            AllureUtils.attachScreenshot(
                    DriverManager.getPage(),
                    "Screenshot da falha - " + context.getDisplayName()
            );

            AllureUtils.attachText(
                    "Detalhes da falha",
                    exception.toString()
            );
        });
    }

    private static final class SuiteResource
            implements ExtensionContext.Store.CloseableResource {

        private SuiteResource() {
            AllureUtils.cleanResults();
        }

        @Override
        public void close() {
            if (!shouldOpenReport() || isCiEnvironment() || !hasAllureResults()) {
                return;
            }

            try {
                new ProcessBuilder(buildAllureCommand())
                        .inheritIO()
                        .start();
            } catch (IOException exception) {
                LOGGER.warn(
                        "Não foi possível abrir o relatório Allure automaticamente.",
                        exception
                );
            }
        }
    }

    private static boolean shouldOpenReport() {
        return Boolean.parseBoolean(
                System.getProperty("allure.open.report", "false")
        );
    }

    private static boolean isCiEnvironment() {
        return "true".equalsIgnoreCase(System.getenv("CI"))
                || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }

    private static boolean hasAllureResults() {
        Path resultsPath = Paths.get("target", "allure-results");

        if (!Files.isDirectory(resultsPath)) {
            return false;
        }

        try (Stream<Path> files = Files.list(resultsPath)) {
            return files.findAny().isPresent();
        } catch (IOException exception) {
            LOGGER.warn("Não foi possível verificar os resultados do Allure.", exception);
            return false;
        }
    }

    private static List<String> buildAllureCommand() {
        String operatingSystem =
                System.getProperty("os.name", "")
                        .toLowerCase(Locale.ROOT);

        if (operatingSystem.contains("win")) {
            return List.of(
                    "cmd",
                    "/c",
                    "allure",
                    "serve",
                    "target/allure-results"
            );
        }

        return List.of(
                "sh",
                "-c",
                "allure serve target/allure-results"
        );
    }
}
