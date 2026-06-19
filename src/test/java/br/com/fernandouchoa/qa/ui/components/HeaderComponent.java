package br.com.fernandouchoa.qa.ui.components;

import com.microsoft.playwright.Locator;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.pages.LoginPage;

public class HeaderComponent {

    private final Page page;

    private final Locator signupLoginLink;
    private final Locator productsLink;
    private final Locator cartLink;

    public HeaderComponent(Page page) {

        this.page = page;

        this.signupLoginLink = page.locator("a[href='/login']");
        this.productsLink = page.locator("a[href='/products']");
        this.cartLink = page.locator("a[href='/view_cart']");
    }
    
    public LoginPage goToLoginPage() {

        signupLoginLink.click();

        return new LoginPage(page);
    }

    public void clickProducts() {
        productsLink.click();
    }

    public void clickCart() {
        cartLink.click();
    }
    
    public ProductsPage goToProductsPage() {
        productsLink.click();

        return new ProductsPage(page);
    }
    
    
}
