package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.locators.ProductDetailsLocators;
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
                locator(ProductDetailsLocators.PRODUCT_INFORMATION);

        this.productName =
                locator(ProductDetailsLocators.PRODUCT_NAME);

        this.productCategory =
                locator(ProductDetailsLocators.PRODUCT_CATEGORY).first();

        this.productPrice =
                locator(ProductDetailsLocators.PRODUCT_PRICE);

        this.productAvailability =
                locator(ProductDetailsLocators.PRODUCT_AVAILABILITY);

        this.addToCartButton =
                locator(ProductDetailsLocators.ADD_TO_CART_BUTTON);

        this.quantityInput =
                locator(ProductDetailsLocators.QUANTITY_INPUT);
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