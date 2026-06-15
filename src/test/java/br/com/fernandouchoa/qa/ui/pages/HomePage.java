package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Page;

import br.com.fernandouchoa.qa.core.config.EnvironmentManager;

public class HomePage extends BasePage {

    public HomePage(Page page) {
        super(page);
    }

    public HomePage open() {
        navigateTo(EnvironmentManager.getBaseUrl());
        return this;
    }

    public boolean isHomePageLoaded() {
        return page.title().contains("Automation Exercise");
    }
    
    
}