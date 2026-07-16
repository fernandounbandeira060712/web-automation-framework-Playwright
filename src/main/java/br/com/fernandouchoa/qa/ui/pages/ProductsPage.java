package br.com.fernandouchoa.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import br.com.fernandouchoa.qa.ui.components.CartModalComponent;
import br.com.fernandouchoa.qa.ui.locators.ProductsLocators;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class ProductsPage extends BasePage {

    private final Locator productsSection;
    private final Locator searchInput;
    private final Locator searchButton;
    private final Locator productCards;

    public ProductsPage(Page page) {
        super(page);

        this.productsSection = locator(ProductsLocators.PRODUCTS_SECTION);
        this.searchInput = locator(ProductsLocators.SEARCH_INPUT);
        this.searchButton = locator(ProductsLocators.SEARCH_BUTTON);
        this.productCards = locator(ProductsLocators.PRODUCT_CARDS);
    }

    @Step("Validar se a página de produtos foi carregada")
    public boolean isLoaded() {
        return isVisible(productsSection);
    }

    @Step("Pesquisar produto")
    public ProductsPage searchProduct(String productName) {
        Allure.parameter("Produto pesquisado", productName);

        fill(searchInput, productName);
        click(searchButton);
        waitForSelector(ProductsLocators.PRODUCT_CARDS);

        return this;
    }

    @Step("Validar se existem produtos exibidos")
    public boolean hasProductsDisplayed() {
        return count(productCards) > 0;
    }

    @Step("Capturar quantidade de produtos exibidos")
    public int getProductsCount() {
        return count(productCards);
    }

    @Step("Visualizar detalhes do produto")
    public ProductDetailsPage viewProductById(String productId) {
        Allure.parameter("ID do produto", productId);

        navigateToPath(ProductsLocators.productDetailsUrl(productId));

        return new ProductDetailsPage(page);
    }

    @Step("Adicionar produto ao carrinho")
    public CartModalComponent addProductToCartById(String productId) {
        Allure.parameter("ID do produto", productId);

        Locator addToCartButton =
                locator(ProductsLocators.addToCartButtonByProductId(productId));

        click(addToCartButton);

        page.waitForSelector(
                ProductsLocators.CART_MODAL,
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        return new CartModalComponent(page);
    }
}