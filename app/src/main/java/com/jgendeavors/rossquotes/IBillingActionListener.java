package com.jgendeavors.rossquotes;

/**
 * The IBillingActionListener interface defines the contract between components who wish to communicate billing actions,
 * e.g. product purchase requests, restore purchase actions, etc..
 */
public interface IBillingActionListener {
    /** Notify that the user intends to purchase the product with the given @productId */
    void onPurchaseAction(String productId);
    void onConsumePurchaseAction(String productId);
}
