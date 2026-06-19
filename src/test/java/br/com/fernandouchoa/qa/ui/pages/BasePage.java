package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;

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
        locator.waitFor();
        locator.click();
    }

    protected void fill(Locator locator, String value) {
        locator.waitFor();
        locator.fill(value);
    }

    protected String getText(Locator locator) {
        locator.waitFor();
        return locator.innerText();
    }

    protected boolean isVisible(Locator locator) {
        return locator.isVisible();
    }

    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected String getTitle() {
        return page.title();
    }

    protected String getCurrentUrl() {
        return page.url();
    }
}
