package br.com.fernandouchoa.qa.core.retry;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import io.qameta.allure.Allure;

public class RetryTestExtension implements InvocationInterceptor {

    private static final int MAX_ATTEMPTS = 2;

    @Override
    public void interceptTestMethod(
            Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {

        Throwable lastThrowable = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                Allure.parameter("Tentativa", attempt + " de " + MAX_ATTEMPTS);
                invocation.proceed();
                return;
            } catch (Throwable throwable) {
                lastThrowable = throwable;

                if (attempt == MAX_ATTEMPTS) {
                    throw lastThrowable;
                }
            }
        }
    }
}