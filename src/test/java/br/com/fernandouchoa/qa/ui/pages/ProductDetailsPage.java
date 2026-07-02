package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import io.qameta.allure.Step;

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

    @Step("Validar se a página de detalhes do produto foi carregada")
    public boolean isLoaded() {
        return isVisible(productInformation);
    }

    @Step("Capturar nome do produto")
    public String getProductName() {
        return getText(productName);
    }

    @Step("Capturar categoria do produto")
    public String getProductCategory() {
        return getText(productCategory);
    }

    @Step("Capturar preço do produto")
    public String getProductPrice() {
        return getText(productPrice);
    }

    @Step("Validar disponibilidade do produto")
    public boolean isAvailabilityDisplayed() {
        return isVisible(productAvailability);
    }

    @Step("Informar quantidade do produto")
    public ProductDetailsPage setQuantity(String quantity) {
        fill(quantityInput, quantity);
        return this;
    }

    @Step("Adicionar produto ao carrinho pela página de detalhes")
    public ProductDetailsPage addToCart() {
        click(addToCartButton);
        return this;
    }
}