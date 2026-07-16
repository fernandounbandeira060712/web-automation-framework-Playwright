package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.ui.locators.AccountLocators;
import io.qameta.allure.Step;

public class AccountPage extends BasePage {

    private final Locator loggedUser;

    public AccountPage(Page page) {
        super(page);

        this.loggedUser =
                locator(AccountLocators.LOGGED_USER);
    }

    @Step("Validar se usuário está logado")
    public boolean isLoaded() {
        return isVisible(loggedUser);
    }

    @Step("Realizar logout do usuário")
    public LoginPage logout() {
        navigateToPath("logout");
        return new LoginPage(page);
    }
}