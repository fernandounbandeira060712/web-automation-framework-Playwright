package br.com.fernandouchoa.qa.ui.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import br.com.fernandouchoa.qa.model.User;
import br.com.fernandouchoa.qa.ui.components.CartModalComponent;
import br.com.fernandouchoa.qa.ui.pages.AccountPage;
import br.com.fernandouchoa.qa.ui.pages.CartPage;
import br.com.fernandouchoa.qa.ui.pages.CheckoutPage;
import br.com.fernandouchoa.qa.ui.pages.HomePage;
import br.com.fernandouchoa.qa.ui.pages.LoginPage;
import br.com.fernandouchoa.qa.ui.pages.ProductDetailsPage;
import br.com.fernandouchoa.qa.ui.pages.ProductsPage;
import br.com.fernandouchoa.qa.utils.AssertUtils;
import br.com.fernandouchoa.qa.utils.TestDataManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class AutomationExerciseHomeTest extends BaseTest {

    @Test
    @Epic("UI Automation")
    @Feature("Login")
    @Story("Login com usuário inválido")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Fernando Uchoa")
    @Tag("Regression")
    @Description("Valida a mensagem apresentada quando o usuário informa credenciais inválidas.")
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

//    @Test
//    @Epic("UI Automation")
//    @Feature("Login")
//    @Story("Login com usuário válido")
//    @Severity(SeverityLevel.BLOCKER)
//    @Owner("Fernando Uchoa")
//    @Tag("Smoke")
//    @Tag("Regression")
//    @Description("Valida que um usuário válido consegue realizar login.")
//    public void deveRealizarLoginComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        User validUser =
//                TestDataManager.getUser("validUser");
//
//        AccountPage accountPage =
//                homePage.header()
//                        .goToLoginPage()
//                        .loginSuccessfully(validUser);
//
//        AssertUtils.assertTrue(
//                accountPage.isLoaded(),
//                "Usuário não foi autenticado.");
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Logout")
//    @Story("Encerrar sessão")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que o usuário consegue finalizar sua sessão.")
//    public void deveRealizarLogoutComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        User validUser =
//                TestDataManager.getUser("validUser");
//
//        AccountPage accountPage =
//                homePage.header()
//                        .goToLoginPage()
//                        .loginSuccessfully(validUser);
//
//        LoginPage loginPage =
//                accountPage.logout();
//
//        AssertUtils.assertTrue(
//                loginPage.isLoginPageLoaded(),
//                "Usuário não foi deslogado.");
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Produtos")
//    @Story("Pesquisa de produtos")
//    @Severity(SeverityLevel.NORMAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que a página de produtos carrega e exibe produtos disponíveis.")
//    public void devePesquisarProdutoComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        ProductsPage productsPage =
//                homePage.header()
//                        .goToProductsPage();
//
//        AssertUtils.assertTrue(
//                productsPage.isLoaded(),
//                "Página de produtos não foi carregada."
//        );
//
//        AssertUtils.assertTrue(
//                productsPage.hasProductsDisplayed(),
//                "Nenhum produto foi exibido."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Home")
//    @Story("Carregamento da página inicial")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Smoke")
//    @Tag("Regression")
//    @Description("Valida que a página inicial é carregada com sucesso.")
//    public void deveCarregarHomeComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        AssertUtils.assertTrue(
//                homePage.isLoaded(),
//                "Home Page não foi carregada."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Produtos")
//    @Story("Detalhes do produto")
//    @Severity(SeverityLevel.NORMAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida a exibição das informações detalhadas do produto.")
//    public void deveVisualizarDetalhesDoProdutoComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        ProductDetailsPage productDetailsPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .viewProductById("1");
//
//        AssertUtils.assertTrue(
//                productDetailsPage.isLoaded(),
//                "Página de detalhes do produto não foi carregada."
//        );
//
//        AssertUtils.assertTrue(
//                !productDetailsPage.getProductName().isBlank(),
//                "Nome do produto não foi exibido."
//        );
//
//        AssertUtils.assertTrue(
//                !productDetailsPage.getProductPrice().isBlank(),
//                "Preço do produto não foi exibido."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Carrinho")
//    @Story("Adicionar produto")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que um produto pode ser adicionado ao carrinho.")
//    public void deveAdicionarProdutoAoCarrinho() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        CartModalComponent cartModal =
//
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1");
//
//        AssertUtils.assertTrue(
//                cartModal.isDisplayed(),
//                "Modal de produto adicionado não foi exibido."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Carrinho")
//    @Story("Visualizar carrinho")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que os produtos adicionados aparecem no carrinho.")
//    public void deveVisualizarCarrinhoComProdutoAdicionado() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        CartPage cartPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1")
//                        .viewCart();
//
//        AssertUtils.assertTrue(
//                cartPage.isLoaded(),
//                "Página do carrinho não foi carregada."
//        );
//
//        AssertUtils.assertTrue(
//                cartPage.hasProducts(),
//                "Nenhum produto foi encontrado no carrinho."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Carrinho")
//    @Story("Remover produto")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que um produto pode ser removido do carrinho.")
//    public void deveRemoverProdutoDoCarrinho() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        CartPage cartPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1")
//                        .viewCart();
//
//        cartPage.removeFirstProduct();
//
//        AssertUtils.assertTrue(
//                cartPage.isEmpty(),
//                "Carrinho não está vazio."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Carrinho")
//    @Story("Carrinho vazio")
//    @Severity(SeverityLevel.NORMAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que o carrinho fica vazio após remover o produto.")
//    public void deveRemoverProdutoDoCarrinhoComSucesso() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        CartPage cartPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1")
//                        .viewCart();
//
//        AssertUtils.assertTrue(
//                cartPage.hasProducts(),
//                "Nenhum produto foi encontrado no carrinho."
//        );
//
//        cartPage.removeFirstProduct();
//
//        AssertUtils.assertTrue(
//                cartPage.isEmpty(),
//                "Carrinho não ficou vazio após remover o produto."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Checkout")
//    @Story("Acessar checkout")
//    @Severity(SeverityLevel.BLOCKER)
//    @Owner("Fernando Uchoa")
//    @Tag("Smoke")
//    @Tag("Regression")
//    @Description("Valida que um usuário autenticado consegue acessar a página de checkout após adicionar um produto ao carrinho.")
//    public void deveAcessarCheckoutComProdutoNoCarrinho() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        User validUser =
//                TestDataManager.getUser("validUser");
//
//        homePage.header()
//                .goToLoginPage()
//                .loginSuccessfully(validUser);
//
//        CheckoutPage checkoutPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1")
//                        .viewCart()
//                        .proceedToCheckout();
//
//        AssertUtils.assertTrue(
//                checkoutPage.isLoaded(),
//                "Página de checkout não foi carregada."
//        );
//    }
//
//    @Test
//    @Epic("UI Automation")
//    @Feature("Checkout")
//    @Story("Revisão do pedido")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Fernando Uchoa")
//    @Tag("Regression")
//    @Description("Valida que os produtos adicionados são exibidos na revisão do pedido durante o checkout.")
//    public void deveExibirProdutoNaRevisaoDoCheckout() {
//
//        HomePage homePage = new HomePage(page);
//
//        homePage.open();
//
//        User validUser =
//                TestDataManager.getUser("validUser");
//
//        homePage.header()
//                .goToLoginPage()
//                .loginSuccessfully(validUser);
//
//        CheckoutPage checkoutPage =
//                homePage.header()
//                        .goToProductsPage()
//                        .addProductToCartById("1")
//                        .viewCart()
//                        .proceedToCheckout();
//
//        AssertUtils.assertTrue(
//                checkoutPage.hasOrderItems(),
//                "Produto não foi exibido na revisão do pedido."
//        );
//    }
}