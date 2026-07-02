package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import br.com.fernandouchoa.qa.ui.components.HeaderComponent;
import io.qameta.allure.Step;

public class HomePage extends BasePage {

    private final HeaderComponent header;

    private final Locator slider;
    private final Locator categorySection;
    private final Locator brandsSection;
    private final Locator featuredProducts;

    public HomePage(Page page) {

        super(page);

        this.header =
                new HeaderComponent(page);

        this.slider =
                page.locator("#slider");

        this.categorySection =
                page.locator(".category-products");

        this.brandsSection =
                page.locator(".brands_products");

        this.featuredProducts =
                page.locator(".features_items");
    }

    @Step("Acessar a Home Page")
    public HomePage open() {

        try {
            page.navigate(
                    EnvironmentManager.getBaseUrl(),
                    new Page.NavigateOptions()
                            .setTimeout(15000)
                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
            );
        } catch (Exception exception) {
            page.evaluate("window.stop()");
        }

        return this;
    }

    @Step("Acessar componente Header")
    public HeaderComponent header() {
        return header;
    }

    @Step("Validar se a Home Page foi carregada")
    public boolean isLoaded() {
        return isVisible(slider)
                && isVisible(categorySection)
                && isVisible(brandsSection)
                && isVisible(featuredProducts);
    }
}