package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;

public class ProductsPage extends BasePage {

    private final Locator productsSection;
    private final Locator searchInput;
    private final Locator searchButton;
    private final Locator productCards;

    public ProductsPage(Page page) {
        super(page);

        this.productsSection = page.locator(".features_items");
        this.searchInput = page.locator("#search_product");
        this.searchButton = page.locator("#submit_search");
        this.productCards = page.locator(".product-image-wrapper");
    }

    public boolean isLoaded() {
        return productsSection.isVisible();
    }

    public ProductsPage searchProduct(String productName) {
        searchInput.fill(productName);
        searchButton.click();
        page.waitForSelector(".product-image-wrapper");
        return this;
    }

    public boolean hasProductsDisplayed() {
        return productCards.count() > 0;
    }

    public int getProductsCount() {
        return productCards.count();
    }

    public ProductDetailsPage viewProductById(String productId) {
        page.navigate(EnvironmentManager.getBaseUrl() + "product_details/" + productId);

        return new ProductDetailsPage(page);
    }

    public ProductsPage addProductToCartById(String productId) {
        page.locator("a[data-product-id='" + productId + "']").first().click();
        return this;
    }
}