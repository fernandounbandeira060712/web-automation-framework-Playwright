package br.com.fernandouchoa.qa.ui.tests;

import br.com.fernandouchoa.qa.core.annotations.CriticalTest;
import br.com.fernandouchoa.qa.core.annotations.Regression;
import br.com.fernandouchoa.qa.core.annotations.Smoke;
import br.com.fernandouchoa.qa.model.User;
import br.com.fernandouchoa.qa.ui.components.CartModalComponent;
import br.com.fernandouchoa.qa.ui.pages.ProductDetailsPage;
import br.com.fernandouchoa.qa.utils.AssertUtils;
import br.com.fernandouchoa.qa.utils.TestDataManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("UI Automation")
@Owner("Fernando Uchoa")
public class AutomationExerciseHomeTest extends BaseTest {

    @Regression
    @Feature("Login")
    @Story("Login com usuário inválido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a mensagem apresentada quando o usuário informa credenciais inválidas.")
    public void deveRealizarLogin() {
        loginPage = homePage.header().goToLoginPage();

        User invalidUser = TestDataManager.getUser("invalidUser");

        loginPage.login(invalidUser);

        AssertUtils.assertTrue(
                loginPage.isInvalidLoginMessageDisplayed(),
                "Mensagem de login inválido não foi exibida."
        );
    }

    @Smoke
    @Feature("Login")
    @Story("Login com usuário válido")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida que um usuário válido consegue realizar login.")
    public void deveRealizarLoginComSucesso() {
        User validUser = TestDataManager.getUser("validUser");

        accountPage = homePage.header()
                .goToLoginPage()
                .loginSuccessfully(validUser);

        AssertUtils.assertTrue(
                accountPage.isLoaded(),
                "Usuário não foi autenticado."
        );
    }

    @CriticalTest
    @Feature("Logout")
    @Story("Encerrar sessão")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que o usuário consegue finalizar sua sessão.")
    public void deveRealizarLogoutComSucesso() {
        User validUser = TestDataManager.getUser("validUser");

        accountPage = homePage.header()
                .goToLoginPage()
                .loginSuccessfully(validUser);

        loginPage = accountPage.logout();

        AssertUtils.assertTrue(
                loginPage.isLoginPageLoaded(),
                "Usuário não foi deslogado."
        );
    }

    @Regression
    @Feature("Produtos")
    @Story("Pesquisa de produtos")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a página de produtos carrega e exibe produtos disponíveis.")
    public void devePesquisarProdutoComSucesso() {
        productsPage = homePage.header().goToProductsPage();

        AssertUtils.assertTrue(
                productsPage.isLoaded(),
                "Página de produtos não foi carregada."
        );

        AssertUtils.assertTrue(
                productsPage.hasProductsDisplayed(),
                "Nenhum produto foi exibido."
        );
    }

    @Smoke
    @Feature("Home")
    @Story("Carregamento da página inicial")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que a página inicial é carregada com sucesso.")
    public void deveCarregarHomeComSucesso() {
        AssertUtils.assertTrue(
                homePage.isLoaded(),
                "Home Page não foi carregada."
        );
    }

    @Regression
    @Feature("Produtos")
    @Story("Detalhes do produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida a exibição das informações detalhadas do produto.")
    public void deveVisualizarDetalhesDoProdutoComSucesso() {
        ProductDetailsPage productDetailsPage = homePage.header()
                .goToProductsPage()
                .viewProductById("1");

        AssertUtils.assertTrue(
                productDetailsPage.isLoaded(),
                "Página de detalhes do produto não foi carregada."
        );

        AssertUtils.assertTrue(
                !productDetailsPage.getProductName().isBlank(),
                "Nome do produto não foi exibido."
        );

        AssertUtils.assertTrue(
                !productDetailsPage.getProductPrice().isBlank(),
                "Preço do produto não foi exibido."
        );
    }

    @CriticalTest
    @Feature("Carrinho")
    @Story("Adicionar produto")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um produto pode ser adicionado ao carrinho.")
    public void deveAdicionarProdutoAoCarrinho() {
        CartModalComponent cartModal = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1");

        AssertUtils.assertTrue(
                cartModal.isDisplayed(),
                "Modal de produto adicionado não foi exibido."
        );
    }

    @CriticalTest
    @Feature("Carrinho")
    @Story("Visualizar carrinho")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que os produtos adicionados aparecem no carrinho.")
    public void deveVisualizarCarrinhoComProdutoAdicionado() {
        cartPage = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1")
                .viewCart();

        AssertUtils.assertTrue(
                cartPage.isLoaded(),
                "Página do carrinho não foi carregada."
        );

        AssertUtils.assertTrue(
                cartPage.hasProducts(),
                "Nenhum produto foi encontrado no carrinho."
        );
    }

    @CriticalTest
    @Feature("Carrinho")
    @Story("Remover produto")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um produto pode ser removido do carrinho.")
    public void deveRemoverProdutoDoCarrinho() {
        cartPage = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1")
                .viewCart();

        cartPage.removeFirstProduct();

        AssertUtils.assertTrue(
                cartPage.isEmpty(),
                "Carrinho não está vazio."
        );
    }

    @Regression
    @Feature("Carrinho")
    @Story("Carrinho vazio")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que o carrinho fica vazio após remover o produto.")
    public void deveRemoverProdutoDoCarrinhoComSucesso() {
        cartPage = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1")
                .viewCart();

        AssertUtils.assertTrue(
                cartPage.hasProducts(),
                "Nenhum produto foi encontrado no carrinho."
        );

        cartPage.removeFirstProduct();

        AssertUtils.assertTrue(
                cartPage.isEmpty(),
                "Carrinho não ficou vazio após remover o produto."
        );
    }

    @Smoke
    @Feature("Checkout")
    @Story("Acessar checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida que um usuário autenticado consegue acessar a página de checkout após adicionar um produto ao carrinho.")
    public void deveAcessarCheckoutComProdutoNoCarrinho() {
        User validUser = TestDataManager.getUser("validUser");

        homePage.header()
                .goToLoginPage()
                .loginSuccessfully(validUser);

        checkoutPage = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1")
                .viewCart()
                .proceedToCheckout();

        AssertUtils.assertTrue(
                checkoutPage.isLoaded(),
                "Página de checkout não foi carregada."
        );
    }

    @CriticalTest
    @Feature("Checkout")
    @Story("Revisão do pedido")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que os produtos adicionados são exibidos na revisão do pedido durante o checkout.")
    public void deveExibirProdutoNaRevisaoDoCheckout() {
        User validUser = TestDataManager.getUser("validUser");

        homePage.header()
                .goToLoginPage()
                .loginSuccessfully(validUser);

        checkoutPage = homePage.header()
                .goToProductsPage()
                .addProductToCartById("1")
                .viewCart()
                .proceedToCheckout();

        AssertUtils.assertTrue(
                checkoutPage.hasOrderItems(),
                "Produto não foi exibido na revisão do pedido."
        );
    }
}