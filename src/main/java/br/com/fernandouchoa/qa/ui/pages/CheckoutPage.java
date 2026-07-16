package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.locators.CheckoutLocators;
import io.qameta.allure.Step;

public class CheckoutPage extends BasePage {

    private final Locator addressDetails;
    private final Locator reviewOrder;
    private final Locator orderItems;

    public CheckoutPage(Page page) {
        super(page);

        this.addressDetails =
                locator(CheckoutLocators.ADDRESS_DETAILS);

        this.reviewOrder =
                locator(CheckoutLocators.REVIEW_ORDER);

        this.orderItems =
                locator(CheckoutLocators.ORDER_ITEMS);
    }

    @Step("Validar se a página de checkout foi carregada")
    public boolean isLoaded() {
        return getCurrentUrl().contains("/checkout")
                && isVisible(addressDetails)
                && isVisible(reviewOrder);
    }

    @Step("Validar se existem produtos na revisão do pedido")
    public boolean hasOrderItems() {
        return count(orderItems) > 0;
    }
}