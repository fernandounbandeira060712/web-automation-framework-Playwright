package br.com.fernandouchoa.qa.core.retry;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(METHOD)
@Retention(RUNTIME)
@Test
@ExtendWith(RetryTestExtension.class)
public @interface RetryTest {

    int maxAttempts() default 2;
}
