package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import io.qameta.allure.Step;

public class CartPage extends BasePage {

    private final Locator cartTable;
    private final Locator cartItems;
    private final Locator deleteButtons;
    private final Locator proceedToCheckoutButton;
    private final Locator emptyCartMessage;

    public CartPage(Page page) {
        super(page);

        this.cartTable =
                page.locator("#cart_info");

        this.cartItems =
                page.locator("#cart_info_table tbody tr");

        this.deleteButtons =
                page.locator(".cart_quantity_delete");

        this.proceedToCheckoutButton =
                page.locator(".check_out");

        this.emptyCartMessage =
                page.locator("#empty_cart");
    }

    @Step("Validar se a página do carrinho foi carregada")
    public boolean isLoaded() {
        return isVisible(cartTable);
    }

    @Step("Validar se existem produtos no carrinho")
    public boolean hasProducts() {
        waitForPageLoad();
        return cartItems.count() > 0;
    }

    @Step("Remover o primeiro produto do carrinho")
    public CartPage removeFirstProduct() {

        if (cartItems.count() == 0) {
            waitForPageLoad();
        }

        click(deleteButtons.first());

        page.waitForTimeout(1500);

        return this;
    }

    @Step("Validar se o carrinho está vazio")
    public boolean isEmpty() {
        waitForPageLoad();

        return cartItems.count() == 0
                || emptyCartMessage.isVisible();
    }

    @Step("Prosseguir para o checkout")
    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckoutButton);
        return new CheckoutPage(page);
    }
}