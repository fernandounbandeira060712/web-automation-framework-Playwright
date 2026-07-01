package br.com.fernandouchoa.qa.core.extensions;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureReportExtension implements AfterAllCallback {

    private static final AtomicBoolean REPORT_ALREADY_OPENED =
            new AtomicBoolean(false);

    @Override
    public void afterAll(ExtensionContext context) throws IOException {

        if (isCiEnvironment()) {
            return;
        }

        if (REPORT_ALREADY_OPENED.compareAndSet(false, true)) {
            new ProcessBuilder(
                    "cmd",
                    "/c",
                    "allure serve target/allure-results"
            ).start();
        }
    }

    private boolean isCiEnvironment() {
        return "true".equalsIgnoreCase(System.getenv("CI"))
                || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }
}
