package br.com.fernandouchoa.qa.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

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
        );
    }

    public static void waitUntilHidden(Locator locator) {
        locator.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
        );
    }

    public static void click(Locator locator) {
        waitUntilVisible(locator);
        locator.click();
    }

    public static void fill(Locator locator, String value) {
        waitUntilVisible(locator);
        locator.fill(value);
    }

    public static String getText(Locator locator) {
        waitUntilVisible(locator);
        return locator.innerText();
    }

    public static boolean isVisible(Locator locator) {
        return locator.isVisible();
    }
}