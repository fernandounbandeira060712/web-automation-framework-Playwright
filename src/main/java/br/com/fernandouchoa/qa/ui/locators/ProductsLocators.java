package br.com.fernandouchoa.qa.ui.locators;

public final class ProductsLocators {

    private ProductsLocators() {
    }

    public static final String PRODUCTS_SECTION =
            ".features_items";

    public static final String SEARCH_INPUT =
            "#search_product";

    public static final String SEARCH_BUTTON =
            "#submit_search";

    public static final String PRODUCT_CARDS =
            ".product-image-wrapper";

    public static final String PRODUCT_NAMES =
            ".productinfo p";

    public static final String CART_MODAL =
            "#cartModal";

    public static String addToCartButtonByProductId(String productId) {
        return ".productinfo a[data-product-id='" + productId + "']";
    }

    public static String productDetailsUrl(String productId) {
        return "product_details/" + productId;
    }
}
