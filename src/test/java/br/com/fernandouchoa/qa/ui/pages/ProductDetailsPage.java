package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductDetailsPage extends BasePage {

    private final Locator productInformation;
    private final Locator productName;
    private final Locator productCategory;
    private final Locator productPrice;
    private final Locator productAvailability;
    private final Locator addToCartButton;
    private final Locator quantityInput;

    public ProductDetailsPage(Page page) {
        super(page);

        this.productInformation =
                page.locator(".product-information");

        this.productName =
                page.locator(".product-information h2");

        this.productCategory =
                page.locator(".product-information p").first();

        this.productPrice =
                page.locator(".product-information span span");

        this.productAvailability =
                page.locator(".product-information p:has-text('Availability')");

        this.addToCartButton =
                page.locator("button.cart");

        this.quantityInput =
                page.locator("#quantity");
    }

    public boolean isLoaded() {
        return productInformation.isVisible();
    }

    public String getProductName() {
        return productName.innerText();
    }

    public String getProductCategory() {
        return productCategory.innerText();
    }

    public String getProductPrice() {
        return productPrice.innerText();
    }

    public boolean isAvailabilityDisplayed() {
        return productAvailability.isVisible();
    }

    public ProductDetailsPage setQuantity(String quantity) {
        quantityInput.fill(quantity);
        return this;
    }

    public ProductDetailsPage addToCart() {
        addToCartButton.click();
        return this;
    }
}
