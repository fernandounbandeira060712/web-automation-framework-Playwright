package br.com.fernandouchoa.qa.ui.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import br.com.fernandouchoa.qa.ui.pages.CartPage;

public class CartModalComponent {

    private final Page page;

    private final Locator modal;
    private final Locator continueShoppingButton;
    private final Locator viewCartButton;
    
    public CartModalComponent(Page page) {

        this.page = page;

        this.modal =
                page.locator("#cartModal");

        this.continueShoppingButton =
                page.locator(".close-modal");

        this.viewCartButton =
                page.locator("#cartModal a[href='/view_cart']");
    }

    public CartModalComponent addProductToCartById(String productId) {

        page.locator(".productinfo a[data-product-id='" + productId + "']")
                .click();

        page.waitForSelector("#cartModal", 
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE));

        return new CartModalComponent(page);
    }
    
    public boolean isDisplayed() {
        return modal.isVisible();
    }

    public void continueShopping() {
        continueShoppingButton.click();
    }

    public CartPage viewCart() {

        viewCartButton.click();

        return new CartPage(page);
    }
 }
