package br.com.fernandouchoa.qa.core.retry;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.support.AnnotationSupport;
import org.opentest4j.TestAbortedException;

import io.qameta.allure.Allure;

/**
 * Implementa tentativas sequenciais para métodos anotados com {@link RetryTest}.
 * As tentativas permanecem serializadas mesmo quando a suíte executa em paralelo.
 */
public final class RetryTestExtension
        implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .filter(method -> AnnotationSupport.isAnnotated(method, RetryTest.class))
                .isPresent();
    }

    @Override
    public Stream<TestTemplateInvocationContext>
            provideTestTemplateInvocationContexts(ExtensionContext context) {

        Method testMethod = context.getRequiredTestMethod();
        RetryTest retryTest = AnnotationSupport.findAnnotation(
                testMethod,
                RetryTest.class
        ).orElseThrow();

        int maxAttempts = retryTest.maxAttempts();

        if (maxAttempts < 1) {
            throw new IllegalArgumentException(
                    "maxAttempts deve ser maior ou igual a 1."
            );
        }

        RetryState state = new RetryState();
        String displayName = retryTest.name().isBlank()
                ? testMethod.getName()
                : retryTest.name();

        return IntStream.rangeClosed(1, maxAttempts)
                .mapToObj(attempt -> createInvocationContext(
                        displayName,
                        attempt,
                        maxAttempts,
                        state
                ));
    }

    private TestTemplateInvocationContext createInvocationContext(
            String displayName,
            int attempt,
            int maxAttempts,
            RetryState state) {

        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return String.format(
                        "%s [tentativa %d de %d]",
                        displayName,
                        attempt,
                        maxAttempts
                );
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(
                        new RetryAttemptExtension(
                                attempt,
                                maxAttempts,
                                state
                        )
                );
            }
        };
    }

    private static final class RetryAttemptExtension
            implements BeforeEachCallback,
            BeforeTestExecutionCallback,
            TestExecutionExceptionHandler,
            LifecycleMethodExecutionExceptionHandler,
            AfterEachCallback {

        private final int attempt;
        private final int maxAttempts;
        private final RetryState state;

        private Throwable failure;
        private boolean active;
        private boolean completed;

        private RetryAttemptExtension(
                int attempt,
                int maxAttempts,
                RetryState state) {

            this.attempt = attempt;
            this.maxAttempts = maxAttempts;
            this.state = state;
        }

        @Override
        public void beforeEach(ExtensionContext context) {
            active = state.beginAttempt(attempt);

            if (!active) {
                throw new TestAbortedException(
                        "Teste já concluído em uma tentativa anterior."
                );
            }

            Allure.parameter(
                    "Tentativa",
                    attempt + " de " + maxAttempts
            );
        }

        @Override
        public void beforeTestExecution(ExtensionContext context) {
            if (failure != null) {
                throw retryAbortedException(failure);
            }
        }

        @Override
        public void handleTestExecutionException(
                ExtensionContext context,
                Throwable throwable) throws Throwable {

            if (failure != null && throwable instanceof TestAbortedException) {
                throw throwable;
            }

            if (throwable instanceof TestAbortedException) {
                completeTerminalAttempt();
                throw throwable;
            }

            registerFailure(throwable);

            if (isLastAttempt()) {
                completeFailedAttempt();
                throw throwable;
            }

            throw retryAbortedException(throwable);
        }

        @Override
        public void handleBeforeEachMethodExecutionException(
                ExtensionContext context,
                Throwable throwable) throws Throwable {

            registerFailure(throwable);

            if (isLastAttempt()) {
                completeFailedAttempt();
                throw throwable;
            }
        }

        @Override
        public void handleAfterEachMethodExecutionException(
                ExtensionContext context,
                Throwable throwable) throws Throwable {

            registerFailure(throwable);

            if (isLastAttempt()) {
                completeFailedAttempt();
                throw throwable;
            }
        }

        @Override
        public void afterEach(ExtensionContext context) {
            if (!active || completed) {
                return;
            }

            if (failure == null) {
                state.completeSuccessfulAttempt();
                completed = true;
                return;
            }

            state.completeFailedAttempt(attempt);
            completed = true;

            if (!isLastAttempt()) {
                throw retryAbortedException(failure);
            }
        }

        private boolean isLastAttempt() {
            return attempt >= maxAttempts;
        }

        private void registerFailure(Throwable throwable) {
            if (failure == null) {
                failure = throwable;
                return;
            }

            if (failure != throwable) {
                failure.addSuppressed(throwable);
            }
        }

        private void completeFailedAttempt() {
            if (!completed && active) {
                state.completeFailedAttempt(attempt);
                completed = true;
            }
        }

        private void completeTerminalAttempt() {
            if (!completed && active) {
                state.completeTerminalAttempt();
                completed = true;
            }
        }

        private TestAbortedException retryAbortedException(Throwable cause) {
            return new TestAbortedException(
                    "Tentativa " + attempt
                            + " falhou. Uma nova tentativa será executada.",
                    cause
            );
        }
    }

    private static final class RetryState {

        private final ReentrantLock lock = new ReentrantLock();
        private final Condition turnChanged = lock.newCondition();

        private int nextAttempt = 1;
        private boolean finished;

        private boolean beginAttempt(int attempt) {
            lock.lock();

            while (!finished && attempt != nextAttempt) {
                turnChanged.awaitUninterruptibly();
            }

            if (finished) {
                lock.unlock();
                return false;
            }

            return true;
        }

        private void completeSuccessfulAttempt() {
            finished = true;
            turnChanged.signalAll();
            lock.unlock();
        }

        private void completeTerminalAttempt() {
            finished = true;
            turnChanged.signalAll();
            lock.unlock();
        }

        private void completeFailedAttempt(int attempt) {
            nextAttempt = attempt + 1;
            turnChanged.signalAll();
            lock.unlock();
        }
    }
}
