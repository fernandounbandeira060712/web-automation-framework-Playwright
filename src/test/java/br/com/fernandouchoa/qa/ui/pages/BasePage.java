package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import br.com.fernandouchoa.qa.utils.WaitUtils;

public abstract class BasePage {

    protected final Page page;
    protected final String baseUrl;

    protected BasePage(Page page) {
        this.page = page;
        this.baseUrl = EnvironmentManager.getBaseUrl();
    }

    protected void navigateTo(String url) {
        page.navigate(url);
        waitForPageLoad();
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

    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(page);
    }

    protected void waitForNetworkIdle() {
        WaitUtils.waitForNetworkIdle(page);
    }

    protected String getTitle() {
        return page.title();
    }

    protected String getCurrentUrl() {
        return page.url();
    }
}