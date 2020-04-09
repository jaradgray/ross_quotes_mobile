package com.jgendeavors.rossquotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseState;
import com.anjlab.android.iab.v3.TransactionDetails;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    // Constants
    private static final String TAG = "MainActivity";

    // TODO replace with actual license key from Gooble Play account
    public static final String LICENSE_KEY = null;
    // TODO replace with actual product id
    public static final String PRODUCT_ID_PREMIUM = null;


    // Instance variables
    private BillingProcessor mBillingProcessor;


    // Lifecycle overrides

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar as support action bar
        final Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Connect toolbar to NavController
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(toolbar, navController);

        // Initialize BillingProcessor
        mBillingProcessor = BillingProcessor.newBillingProcessor(this, LICENSE_KEY, this);
        mBillingProcessor.initialize();
    }

    @Override
    protected void onDestroy() {
        // release BillingProcessor
        if (mBillingProcessor != null) {
            mBillingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // required by BillingProcessor
        if (!mBillingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // BillingProcessor.IBillingHandler implementation

    /**
     * Called when requested PRODUCT ID was successfully purchased
     *
     * @param productId
     * @param details
     */
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d(TAG, "onProductPurchased: started");
    }

    /**
     * Called when purchase history was restored and the list of all owned PRODUCT ID's
     * was loaded from Google Play
     */
    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(TAG, "onPurchaseHistoryRestored: started");
    }

    /**
     * Called when some error occurred. See Constants class for more details
     *
     * Note - this includes handling the case where the user canceled the buy dialog:
     * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
     *
     * @param errorCode
     * @param error
     */
    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d(TAG, "onBillingError: started");
    }

    /**
     * Called when BillingProcessor was initialized and it's ready to purchase
     */
    @Override
    public void onBillingInitialized() {
        Log.d(TAG, "onBillingInitialized: started");

        // refresh BillingProcessor's list of cached products
        mBillingProcessor.loadOwnedPurchasesFromGoogle();
    }


    // API methods

    /**
     * Returns true iff the BillingProcessor is not null, is initialized, and the given @productId
     * has a TransactionDetails object associated with it whose purchaseState is either
     * PurchasedSuccessfully or Refunded. Otherwise returns false.
     *
     * @param productId
     * @return
     */
    public boolean isProductPurchased(String productId) {
        if (mBillingProcessor == null || !mBillingProcessor.isInitialized()) return false;

        // get TransactionDetails for the product
        TransactionDetails transactionDetails = mBillingProcessor.getPurchaseTransactionDetails(productId);
        if (transactionDetails == null) return false;

        // return result based on purchase state
        PurchaseState purchaseState = transactionDetails.purchaseInfo.purchaseData.purchaseState;
        switch (purchaseState) {
            case PurchasedSuccessfully:
            case Refunded:
                return true; // grant access for successful and refunded purchases
            default:
                return false; // deny access for everything else
        }
    }
}
