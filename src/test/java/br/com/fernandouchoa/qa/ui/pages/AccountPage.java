package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;
import io.qameta.allure.Step;

public class AccountPage extends BasePage {

    private final Locator loggedUser;

    public AccountPage(Page page) {
        super(page);

        this.loggedUser =
                page.locator("a:has-text('Logged in as')");
    }

    @Step("Validar se usuário está logado")
    public boolean isLoaded() {
        return isVisible(loggedUser);
    }

    @Step("Realizar logout do usuário")
    public LoginPage logout() {

        page.navigate(
                EnvironmentManager.getBaseUrl() + "logout",
                new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        return new LoginPage(page);
    }
}