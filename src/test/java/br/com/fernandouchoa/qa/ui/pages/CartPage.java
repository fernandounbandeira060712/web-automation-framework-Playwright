package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage extends BasePage {

    private final Locator cartTable;
    private final Locator cartItems;
    private final Locator deleteButtons;

    public CartPage(Page page) {
        super(page);

        this.cartTable =
                page.locator("#cart_info");

        this.cartItems =
                page.locator(".cart_info tbody tr");

        this.deleteButtons =
                page.locator(".cart_quantity_delete");
    }

    public boolean isLoaded() {
        return cartTable.isVisible();
    }

    public boolean hasProducts() {
        return cartItems.count() > 0;
    }

    public CartPage removeFirstProduct() {
        deleteButtons.first().click();
        return this;
    }

    public boolean isEmpty() {
        return cartItems.count() == 0;
    }
}