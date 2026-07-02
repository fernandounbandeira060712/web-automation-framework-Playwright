package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.components.HeaderComponent;
import br.com.fernandouchoa.qa.ui.locators.HomeLocators;
import io.qameta.allure.Step;

public class HomePage extends BasePage {

    private final HeaderComponent header;

    private final Locator slider;
    private final Locator categorySection;
    private final Locator brandsSection;
    private final Locator featuredProducts;

    public HomePage(Page page) {
        super(page);

        this.header = new HeaderComponent(page);

        this.slider = locator(HomeLocators.SLIDER);
        this.categorySection = locator(HomeLocators.CATEGORY_SECTION);
        this.brandsSection = locator(HomeLocators.BRANDS_SECTION);
        this.featuredProducts = locator(HomeLocators.FEATURED_PRODUCTS);
    }

    @Step("Acessar a Home Page")
    public HomePage open() {
        navigateTo(baseUrl);
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