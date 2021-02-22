package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.pref;


public class subscribe extends AppCompatActivity implements PurchasesUpdatedListener {

    private static final String TAG = "subscribe";

    private BillingClient billingClient;

    private SkuDetails skuDetails_subscribe;
    private final String skuID = "subsribe";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_popup);

        findViewById(R.id.tv_subscribe).setOnClickListener(OnClickListener);

        MySoundPlayer.initSounds(getApplicationContext());

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    // The BillingClient is ready. You can query purchases here.
                    // 구글 상품 정보들의 ID를 만들어 줌
                    List<String> skuList = new ArrayList<> ();
                    skuList.add(skuID);

                    // SkuDetailsList 객체를 만듬
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);

                    // 비동기 상태로 앱의 정보를 가지고 옴
                    billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            String price = skuDetails.getPrice();

                                            if(skuID.equals(sku)) {
                                                skuDetails_subscribe = skuDetails;

                                                Log.e(TAG, "onSkuDetailsResponse: 확인 skuDetails_subscribe " + skuDetails_subscribe);
                                            }
                                        }
                                    }
                                }});
                } else {
                    billingClient.startConnection(this);

                    Log.e(TAG, "onBillingSetupFinished: 확인 skuID"+ skuID);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "구글 결제 서버와 접속이 끊어졌습니다.");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                billingClient.startConnection(this);
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        //결제에 성공한 경우
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(TAG, "결제에 성공했으며, 아래에 구매한 상품들이 나열됨");
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
                subscribing(true);
            }
            // 사용자가 결제를 취소한 경우
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "사용자에 의해 결제취소");
            subscribing(false);
        } else {
            // 그 외에 다른 결제 실패 이유
            Log.d(TAG, "결제가 취소 되었습니다. 종료코드: " + billingResult.getResponseCode());
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                // ex 해당 아이템에 대해 소모되지 않은 결제가 있을시
            }
        }
    }

    //실제 구입 처리를 하는 메소드
    private void doBillingFlow(SkuDetails skuDetails) {

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        billingClient.launchBillingFlow(this, flowParams);
        /*if(responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult purchasesResult = BillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            onPurchasesUpdated(BillingClient.BillingResponse.OK, purchasesResult.getPurchasesList());
        }*/
    }

    // 결제 요청 후 상품에대해 소비시켜주는 함수
    void handlePurchase(Purchase purchase) {
        String purchaseToken;
        purchaseToken = purchase.getPurchaseToken();
        billingClient.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build(), consumeListener);
    }

    ConsumeResponseListener consumeListener = new ConsumeResponseListener() {
        @Override
        public void onConsumeResponse(BillingResult billingResult, @NonNull String purchaseToken) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "상품을 성공적으로 소모하였습니다. 소모된 상품 => " + purchaseToken);
            } else {
                Log.d(TAG, "상품 소모에 실패하였습니다. 오류코드 (" + billingResult.getResponseCode() + "), 대상 상품 코드: " + purchaseToken);
                // Handle the success of the consume operation.
                // For example, increase the number of coins inside the user's basket.
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private final View.OnClickListener OnClickListener = new View.OnClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint({"DefaultLocale", "SetTextI18n", "NonConstantResourceId"})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_subscribe:
                    doBillingFlow(skuDetails_subscribe);
                    break;
            }
        }
    };

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //구독 버튼 눌렀을때,
    private void subscribing(boolean bool) {
        MainActivity.subscribing = bool;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    public void mOnConfirm(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
