package br.com.fernandouchoa.qa.ui.locators;

public final class CartLocators {

    private CartLocators() {
    }

    public static final String CART_TABLE =
            "#cart_info";

    public static final String CART_ITEMS =
            "#cart_info_table tbody tr";

    public static final String DELETE_BUTTONS =
            ".cart_quantity_delete";

    public static final String PROCEED_TO_CHECKOUT_BUTTON =
            ".check_out";
}
