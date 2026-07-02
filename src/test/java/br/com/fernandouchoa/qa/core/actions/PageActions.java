package br.com.fernandouchoa.qa.core.actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import br.com.fernandouchoa.qa.utils.WaitUtils;

public class PageActions {

    private final Page page;

    public PageActions(Page page) {
        this.page = page;
    }

    public Locator locator(String selector) {
        return page.locator(selector);
    }

    public Locator first(String selector) {
        return locator(selector).first();
    }

    public Locator nth(String selector, int index) {
        return locator(selector).nth(index);
    }

    public void navigateTo(String url) {
        page.navigate(
                url,
                new Page.NavigateOptions()
                        .setTimeout(60000)
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        WaitUtils.waitForPageLoad(page);
    }

    public void click(Locator locator) {
        WaitUtils.click(locator);
    }

    public void fill(Locator locator, String value) {
        WaitUtils.fill(locator, value);
    }

    public String getText(Locator locator) {
        return WaitUtils.getText(locator);
    }

    public boolean isVisible(Locator locator) {
        return WaitUtils.isVisible(locator);
    }

    public int count(Locator locator) {
        return WaitUtils.count(locator);
    }

    public void waitForPageLoad() {
        WaitUtils.waitForPageLoad(page);
    }

    public void waitForNetworkIdle() {
        WaitUtils.waitForNetworkIdle(page);
    }

    public void waitForSelector(String selector) {
        WaitUtils.waitForSelector(page, selector);
    }

    public String getTitle() {
        return page.title();
    }

    public String getCurrentUrl() {
        return page.url();
    }
}
