package br.com.fernandouchoa.qa.utils;

import org.junit.jupiter.api.Assertions;

import com.microsoft.playwright.Page;

public final class AssertUtils {

    private AssertUtils() {
    }

    public static void assertTrue(
            boolean condition,
            String message) {

        Assertions.assertTrue(condition, message);
    }

    public static void assertFalse(
            boolean condition,
            String message) {

        Assertions.assertFalse(condition, message);
    }

    public static void assertEquals(
            Object expected,
            Object actual,
            String message) {

        Assertions.assertEquals(expected, actual, message);
    }

    public static void assertNotBlank(
            String value,
            String message) {

        Assertions.assertNotNull(value, message);
        Assertions.assertFalse(value.isBlank(), message);
    }

    public static void assertUrlContains(
            Page page,
            String expectedText) {

        Assertions.assertTrue(
                page.url().contains(expectedText),
                "URL atual não contém o texto esperado: " + expectedText
                        + " | URL atual: " + page.url()
        );
    }

    public static void assertTitleContains(
            Page page,
            String expectedText) {

        Assertions.assertTrue(
                page.title().contains(expectedText),
                "Título atual não contém o texto esperado: " + expectedText
                        + " | Título atual: " + page.title()
        );
    }
}