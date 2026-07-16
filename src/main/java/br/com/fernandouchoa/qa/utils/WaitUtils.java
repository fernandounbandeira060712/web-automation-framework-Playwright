package br.com.fernandouchoa.qa.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;

public final class WaitUtils {

    private WaitUtils() {
    }

    public static void waitForPageLoad(Page page) {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public static void waitForNetworkIdle(Page page) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public static void waitUntilVisible(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static void waitUntilHidden(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static void waitUntilAttached(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static void click(Locator locator) {
        waitUntilVisible(locator);
        locator.scrollIntoViewIfNeeded();
        locator.click();
    }

    public static void doubleClick(Locator locator) {
        waitUntilVisible(locator);
        locator.scrollIntoViewIfNeeded();
        locator.dblclick();
    }

    public static void hover(Locator locator) {
        waitUntilVisible(locator);
        locator.scrollIntoViewIfNeeded();
        locator.hover();
    }

    public static void fill(Locator locator, String value) {
        waitUntilVisible(locator);
        locator.fill("");
        locator.fill(value);
    }

    public static void clear(Locator locator) {
        waitUntilVisible(locator);
        locator.fill("");
    }

    public static String getText(Locator locator) {
        waitUntilVisible(locator);
        return locator.innerText();
    }

    public static String getInputValue(Locator locator) {
        waitUntilVisible(locator);
        return locator.inputValue();
    }

    public static boolean isVisible(Locator locator) {
        try {
            return locator.isVisible();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isHidden(Locator locator) {
        try {
            return locator.isHidden();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isEnabled(Locator locator) {
        waitUntilVisible(locator);
        return locator.isEnabled();
    }

    public static void waitForText(Locator locator, String expectedText) {
        waitUntilVisible(locator);

        if (!locator.innerText().contains(expectedText)) {
            throw new AssertionError(
                    "Texto esperado não encontrado. Esperado: "
                            + expectedText
                            + " | Atual: "
                            + locator.innerText()
            );
        }
    }

    public static void waitForUrlContains(Page page, String expectedText) {
        page.waitForURL(
                url -> url.contains(expectedText),
                new Page.WaitForURLOptions()
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static void waitForSelector(Page page, String selector) {
        page.waitForSelector(
                selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static void waitForSelectorHidden(Page page, String selector) {
        page.waitForSelector(
                selector,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(EnvironmentManager.getTimeout())
        );
    }

    public static int count(Locator locator) {
        return locator.count();
    }
}