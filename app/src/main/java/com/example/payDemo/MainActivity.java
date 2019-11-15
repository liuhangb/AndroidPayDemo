package com.example.payDemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.example.payDemo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    private BillingClient mBillingClient;
    private Button mStartBillingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartBillingBtn = findViewById(R.id.start_billing);
        mStartBillingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querySkuDetailsAsync(new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        Logger.i("onSkuDetailsResponse: " + skuDetailsList.size());

                    }
                });
//
//                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                        .setSkuDetails(skuDetails)
//                        .build();
//                mBillingClient.launchBillingFlow(getApplicationContext(), )
            }

        });
        startBillingConnection();

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

    }

    private void startBillingConnection() {
        if (mBillingClient == null) {
            mBillingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build();
            mBillingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    Logger.i("onBillingSetupFinished: " + billingResult.toString());
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    Logger.e("onBillingServiceDisconnected");

                }
            });
        }
    }

    private void querySkuDetailsAsync(SkuDetailsResponseListener responseListener) {
        List<String> skuList = new ArrayList<>();
        skuList.add("premium_upgrade");
        skuList.add("gas");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(), responseListener);

    }
}
