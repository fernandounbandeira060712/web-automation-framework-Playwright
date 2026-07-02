package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import br.com.fernandouchoa.qa.utils.WaitUtils;

public abstract class BasePage {

    protected final Page page;
    protected final String baseUrl;

    protected BasePage(Page page) {
        this.page = page;
        this.baseUrl = EnvironmentManager.getBaseUrl();
    }

    protected Locator locator(String selector) {
        return page.locator(selector);
    }

    protected Locator first(String selector) {
        return locator(selector).first();
    }

    protected Locator nth(String selector, int index) {
        return locator(selector).nth(index);
    }

    protected void navigateTo(String url) {
        page.navigate(
                url,
                new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        waitForPageLoad();
    }

    protected void navigateToPath(String path) {
        navigateTo(baseUrl + path);
    }

    protected void click(Locator locator) {
        WaitUtils.click(locator);
    }

    protected void fill(Locator locator, String value) {
        WaitUtils.fill(locator, value);
    }

    protected String getText(Locator locator) {
        return WaitUtils.getText(locator);
    }

    protected boolean isVisible(Locator locator) {
        return WaitUtils.isVisible(locator);
    }

    protected int count(Locator locator) {
        return locator.count();
    }

    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(page);
    }

    protected void waitForNetworkIdle() {
        WaitUtils.waitForNetworkIdle(page);
    }

    protected void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    protected String getTitle() {
        return page.title();
    }

    protected String getCurrentUrl() {
        return page.url();
    }
}