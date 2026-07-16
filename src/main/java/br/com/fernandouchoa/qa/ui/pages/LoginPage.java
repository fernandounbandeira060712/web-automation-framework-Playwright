package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.model.User;
import br.com.fernandouchoa.qa.ui.locators.LoginLocators;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    private final Locator emailField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator loginErrorMessage;

    public LoginPage(Page page) {
        super(page);

        this.emailField =
                page.locator(LoginLocators.EMAIL_FIELD);

        this.passwordField =
                page.locator(LoginLocators.PASSWORD_FIELD);

        this.loginButton =
                page.locator(LoginLocators.LOGIN_BUTTON);

        this.loginErrorMessage =
                page.locator(LoginLocators.LOGIN_ERROR_MESSAGE);
    }

    @Step("Validar se página de Login foi carregada")
    public boolean isLoginPageLoaded() {
        return getCurrentUrl().contains("/login");
    }

    @Step("Preencher email do usuário")
    public LoginPage fillEmail(String email) {
        fill(emailField, email);
        return this;
    }

    @Step("Preencher senha do usuário")
    public LoginPage fillPassword(String password) {
        fill(passwordField, password);
        return this;
    }

    @Step("Clicar no botão Login")
    public LoginPage clickLogin() {
        click(loginButton);
        return this;
    }

    @Step("Realizar tentativa de login com usuário inválido")
    public LoginPage login(User user) {
        Allure.parameter("Email utilizado", user.getEmail());

        fillEmail(user.getEmail());
        fillPassword(user.getPassword());
        clickLogin();

        return this;
    }

    @Step("Realizar login com usuário válido")
    public AccountPage loginSuccessfully(User user) {
        Allure.parameter("Email utilizado", user.getEmail());

        fillEmail(user.getEmail());
        fillPassword(user.getPassword());
        click(loginButton);

        return new AccountPage(page);
    }

    @Step("Validar mensagem de login inválido")
    public boolean isInvalidLoginMessageDisplayed() {
        return isVisible(loginErrorMessage);
    }
}