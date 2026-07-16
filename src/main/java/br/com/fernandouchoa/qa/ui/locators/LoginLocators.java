package br.com.fernandouchoa.qa.ui.locators;

public final class LoginLocators {

    private LoginLocators() {
    }

    public static final String EMAIL_FIELD =
            "input[data-qa='login-email']";

    public static final String PASSWORD_FIELD =
            "input[data-qa='login-password']";

    public static final String LOGIN_BUTTON =
            "button[data-qa='login-button']";

    public static final String LOGIN_ERROR_MESSAGE =
            "p:has-text('Your email or password is incorrect!')";
}