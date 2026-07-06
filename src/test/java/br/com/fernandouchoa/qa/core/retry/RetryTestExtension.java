package br.com.fernandouchoa.qa.core.retry;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import io.qameta.allure.Allure;

public class RetryTestExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(
            Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {

        RetryTest retryTest =
                extensionContext.getRequiredTestMethod()
                        .getAnnotation(RetryTest.class);

        int maxAttempts = retryTest.maxAttempts();
        Throwable lastThrowable = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            try {
                Allure.parameter("Tentativa", attempt + " de " + maxAttempts);
                invocation.proceed();
                return;

            } catch (Throwable throwable) {
                lastThrowable = throwable;

                System.out.println(
                        "Teste falhou na tentativa "
                                + attempt
                                + " de "
                                + maxAttempts
                                + ": "
                                + extensionContext.getDisplayName()
                );

                if (attempt == maxAttempts) {
                    throw lastThrowable;
                }
            }
        }
    }
}