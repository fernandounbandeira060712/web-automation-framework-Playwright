package br.com.fernandouchoa.qa.core.retry;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.ExtensionContext;

public class RetryTestExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .map(method -> method.isAnnotationPresent(RetryTest.class))
                .orElse(false);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
            ExtensionContext context) {

        RetryTest retryTest =
                context.getRequiredTestMethod().getAnnotation(RetryTest.class);

        return IntStream.rangeClosed(1, retryTest.maxAttempts())
                .mapToObj(attempt -> invocationContext(attempt, retryTest.maxAttempts()));
    }

    private TestTemplateInvocationContext invocationContext(
            int attempt,
            int maxAttempts) {

        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return "Tentativa " + attempt + " de " + maxAttempts;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(new RetryAttemptExtension(attempt, maxAttempts));
            }
        };
    }
}
