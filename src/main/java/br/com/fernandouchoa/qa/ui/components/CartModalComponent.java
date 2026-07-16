package br.com.fernandouchoa.qa.ui.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.locators.CartModalLocators;
import br.com.fernandouchoa.qa.ui.pages.CartPage;
import br.com.fernandouchoa.qa.utils.WaitUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.qameta.allure.Step;

public class CartModalComponent {

    private final Page page;

    private final Locator modal;
    private final Locator continueShoppingButton;
    private final Locator viewCartButton;

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "A instância Page do Playwright precisa ser compartilhada com páginas e componentes."
    )
    public CartModalComponent(Page page) {
        this.page = page;

        this.modal =
                page.locator(CartModalLocators.MODAL);

        this.continueShoppingButton =
                page.locator(CartModalLocators.CONTINUE_SHOPPING_BUTTON);

        this.viewCartButton =
                page.locator(CartModalLocators.VIEW_CART_BUTTON);
    }

    @Step("Validar se o modal de produto adicionado foi exibido")
    public boolean isDisplayed() {
        return WaitUtils.isVisible(modal);
    }

    @Step("Continuar comprando")
    public void continueShopping() {
        WaitUtils.click(continueShoppingButton);
    }

    @Step("Visualizar carrinho pelo modal")
    public CartPage viewCart() {
        WaitUtils.click(viewCartButton);
        return new CartPage(page);
    }

    @Step("Aguardar modal de produto adicionado")
    public CartModalComponent waitUntilDisplayed() {
        WaitUtils.waitForSelector(
                page,
                CartModalLocators.MODAL
        );

        return this;
    }
}