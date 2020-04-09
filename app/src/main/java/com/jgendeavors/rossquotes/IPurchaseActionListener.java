package com.jgendeavors.rossquotes;

/**
 * The IPurchaseActionListener interface defines the contract between components who wish to communicate purchase actions,
 * e.g. when the user clicks the positive button in the PremiumDialog.
 */
public interface IPurchaseActionListener {
    /** Notify that the user intends to purchase the product with the given @productId */
    void onPurchaseAction(String productId);
}
