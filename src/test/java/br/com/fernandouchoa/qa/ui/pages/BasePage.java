package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.core.actions.PageActions;
import br.com.fernandouchoa.qa.core.config.EnvironmentManager;

public abstract class BasePage {

    protected final Page page;
    protected final String baseUrl;
    protected final PageActions actions;

    protected BasePage(Page page) {
        this.page = page;
        this.baseUrl = EnvironmentManager.getBaseUrl();
        this.actions = new PageActions(page);
    }

    protected Locator locator(String selector) {
        return actions.locator(selector);
    }

    protected Locator first(String selector) {
        return actions.first(selector);
    }

    protected Locator nth(String selector, int index) {
        return actions.nth(selector, index);
    }

    protected void navigateTo(String url) {
        actions.navigateTo(url);
    }

    protected void navigateToPath(String path) {
        navigateTo(baseUrl + path);
    }

    protected void click(Locator locator) {
        actions.click(locator);
    }

    protected void fill(Locator locator, String value) {
        actions.fill(locator, value);
    }

    protected String getText(Locator locator) {
        return actions.getText(locator);
    }

    protected boolean isVisible(Locator locator) {
        return actions.isVisible(locator);
    }

    protected int count(Locator locator) {
        return actions.count(locator);
    }

    protected void waitForPageLoad() {
        actions.waitForPageLoad();
    }

    protected void waitForNetworkIdle() {
        actions.waitForNetworkIdle();
    }

    protected void waitForSelector(String selector) {
        actions.waitForSelector(selector);
    }

    protected String getTitle() {
        return actions.getTitle();
    }

    protected String getCurrentUrl() {
        return actions.getCurrentUrl();
    }
}