package br.com.fernandouchoa.qa.core.retry;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import io.qameta.allure.Allure;

public class RetryAttemptExtension implements TestExecutionExceptionHandler {

    private final int attempt;
    private final int maxAttempts;

    public RetryAttemptExtension(int attempt, int maxAttempts) {
        this.attempt = attempt;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void handleTestExecutionException(
            ExtensionContext context,
            Throwable throwable) throws Throwable {

        Allure.parameter("Tentativa", attempt + " de " + maxAttempts);

        if (attempt < maxAttempts) {
            System.out.println(
                    "Teste falhou na tentativa "
                            + attempt
                            + " de "
                            + maxAttempts
                            + ": "
                            + context.getDisplayName()
            );
        }

        throw throwable;
    }
}
