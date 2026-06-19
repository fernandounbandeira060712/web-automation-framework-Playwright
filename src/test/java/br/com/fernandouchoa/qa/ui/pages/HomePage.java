package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.components.HeaderComponent;

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

    public void open() {
        page.navigate(baseUrl);
    }

    public HeaderComponent header() {
        return header;
    }

    public boolean isLoaded() {

        return slider.isVisible()
                && categorySection.isVisible()
                && brandsSection.isVisible()
                && featuredProducts.isVisible();
    }
}