package br.com.fernandouchoa.qa.ui.tests;

import org.junit.jupiter.api.Test;

import br.com.fernandouchoa.qa.model.User;
import br.com.fernandouchoa.qa.ui.pages.AccountPage;
import br.com.fernandouchoa.qa.ui.pages.HomePage;
import br.com.fernandouchoa.qa.ui.pages.LoginPage;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import br.com.fernandouchoa.qa.utils.AssertUtils;
import br.com.fernandouchoa.qa.utils.TestDataManager;

public class AutomationExerciseHomeTest extends BaseTest {

    @Test
    public void deveRealizarLogin() {

        HomePage homePage = new HomePage(page);

        homePage.open();

        LoginPage loginPage =
                homePage.header()
                        .goToLoginPage();

        User invalidUser =
                TestDataManager.getUser("invalidUser");

        loginPage.login(invalidUser);

        AssertUtils.assertTrue(
                loginPage.isInvalidLoginMessageDisplayed(),
                "Mensagem de login inválido não foi exibida."
        );
    }
    
    @Test
    public void deveRealizarLoginComSucesso() {

        HomePage homePage = new HomePage(page);

        homePage.open();

        User validUser =
                TestDataManager.getUser("validUser");

        AccountPage accountPage =
                homePage.header()
                        .goToLoginPage()
                        .loginSuccessfully(validUser);

        AssertUtils.assertTrue(
                accountPage.isLoaded(),
                "Usuário não foi autenticado.");
    }
    
    @Test
    public void deveRealizarLogoutComSucesso() {

        HomePage homePage = new HomePage(page);

        homePage.open();

        User validUser =
                TestDataManager.getUser("validUser");

        AccountPage accountPage =
                homePage.header()
                        .goToLoginPage()
                        .loginSuccessfully(validUser);

        LoginPage loginPage =
                accountPage.logout();

        AssertUtils.assertTrue(
                loginPage.isLoginPageLoaded(),
                "Usuário não foi deslogado.");
    }
    
    @Test
    public void devePesquisarProdutoComSucesso() {

        HomePage homePage = new HomePage(page);

        homePage.open();

        ProductsPage productsPage =
                homePage.header()
                        .goToProductsPage();

        AssertUtils.assertTrue(
                productsPage.isLoaded(),
                "Página de produtos não foi carregada."
        );

        AssertUtils.assertTrue(
                productsPage.hasProductsDisplayed(),
                "Nenhum produto foi exibido."
        );
    
    }
    
    @Test
    public void deveCarregarHomeComSucesso() {

        HomePage homePage = new HomePage(page);

        homePage.open();

        AssertUtils.assertTrue(
                homePage.isLoaded(),
                "Home Page não foi carregada."
        );
    }
}