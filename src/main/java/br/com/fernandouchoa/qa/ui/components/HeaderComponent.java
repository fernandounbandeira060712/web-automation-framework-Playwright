package br.com.fernandouchoa.qa.ui.components;

import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.core.actions.PageActions;
import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import br.com.fernandouchoa.qa.ui.locators.HeaderLocators;
import br.com.fernandouchoa.qa.ui.pages.CartPage;
import br.com.fernandouchoa.qa.ui.pages.LoginPage;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import io.qameta.allure.Step;

public class HeaderComponent {

    private final Page page;
    private final PageActions actions;

    public HeaderComponent(Page page) {
        this.page = page;
        this.actions = new PageActions(page);
    }

    @Step("Acessar página de Login")
    public LoginPage goToLoginPage() {
        navigateTo(HeaderLocators.LOGIN_PATH);
        return new LoginPage(page);
    }

    @Step("Acessar página de Produtos")
    public ProductsPage goToProductsPage() {
        navigateTo(HeaderLocators.PRODUCTS_PATH);
        return new ProductsPage(page);
    }

    @Step("Acessar página do Carrinho")
    public CartPage goToCartPage() {
        navigateTo(HeaderLocators.CART_PATH);
        return new CartPage(page);
    }

    private void navigateTo(String path) {
        actions.navigateTo(EnvironmentManager.getBaseUrl() + path);
    }
}