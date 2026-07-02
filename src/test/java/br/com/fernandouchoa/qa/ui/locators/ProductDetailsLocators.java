package br.com.fernandouchoa.qa.ui.locators;

public final class ProductDetailsLocators {

    private ProductDetailsLocators() {
    }

    public static final String PRODUCT_INFORMATION =
            ".product-information";

    public static final String PRODUCT_NAME =
            ".product-information h2";

    public static final String PRODUCT_CATEGORY =
            ".product-information p";

    public static final String PRODUCT_PRICE =
            ".product-information span span";

    public static final String PRODUCT_AVAILABILITY =
            ".product-information p:has-text('Availability')";

    public static final String ADD_TO_CART_BUTTON =
            "button.cart";

    public static final String QUANTITY_INPUT =
            "#quantity";
}