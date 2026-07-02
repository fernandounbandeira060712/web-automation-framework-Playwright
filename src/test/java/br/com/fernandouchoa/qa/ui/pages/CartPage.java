package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.locators.CartLocators;
import io.qameta.allure.Step;

public class CartPage extends BasePage {

    private final Locator cartTable;
    private final Locator cartItems;
    private final Locator deleteButtons;
    private final Locator proceedToCheckoutButton;

    public CartPage(Page page) {
        super(page);

        this.cartTable =
                locator(CartLocators.CART_TABLE);

        this.cartItems =
                locator(CartLocators.CART_ITEMS);

        this.deleteButtons =
                locator(CartLocators.DELETE_BUTTONS);

        this.proceedToCheckoutButton =
                locator(CartLocators.PROCEED_TO_CHECKOUT_BUTTON);
    }

    @Step("Validar se a página do carrinho foi carregada")
    public boolean isLoaded() {
        return isVisible(cartTable);
    }

    @Step("Validar se existem produtos no carrinho")
    public boolean hasProducts() {
        return count(cartItems) > 0;
    }

    @Step("Remover o primeiro produto do carrinho")
    public CartPage removeFirstProduct() {
        click(deleteButtons.first());
        page.waitForTimeout(1000);
        return this;
    }

    @Step("Validar se o carrinho está vazio")
    public boolean isEmpty() {
        return count(cartItems) == 0;
    }

    @Step("Prosseguir para o checkout")
    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckoutButton);
        return new CheckoutPage(page);
    }
}