package com.bbmsl.payapidemo.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.alipay.sdk.app.PayTask;
import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.constants.MerchantDetail;
import com.bbmsl.payapidemo.databinding.FragmentDirectsaleBinding;
import com.bbmsl.payapidemo.network.RetrofitClient;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;
import com.bbmsl.payapidemo.network.model.directsales.PayResult;
import com.bbmsl.payapidemo.constants.enums.PaymentTerminal;
import com.bbmsl.payapidemo.constants.enums.PaymentType;
import com.bbmsl.payapidemo.network.model.responses.SaleResponse;
import com.bbmsl.payapidemo.network.model.directsales.WechatOrderRes;
import com.bbmsl.payapidemo.network.model.requests.OnlinePayRequest;
import com.bbmsl.payapidemo.network.model.requests.OrderRequest;
import com.bbmsl.payapidemo.ui.components.FragmentCommonInputs;
import com.bbmsl.payapidemo.ui.main.BaseFragment;
import com.bbmsl.payapidemo.util.SignUtils;
import com.bbmsl.payapidemo.util.StringUtil;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_FAIL;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_HOST;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_SUCCESS;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_CHECKOUT_URL;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_KEYSTATUS;

public class FragmentDirectSale extends BaseFragment {
    private static final String TAG = "FragmentDirectSale";

    FragmentDirectsaleBinding binding;

    private IWXAPI wechat_api;

    /**
     * For WechatPay, The merchant can choose between WAP mode and SDK mode.
     */
    final PaymentTerminal terminalmode = PaymentTerminal.WAP;

    /**
     * Direct sales uses SDK provided by Alipay and WechatPay
     * The initialization of the SDK is done at onCreate
     */
    private void initPartnerSDK(){
        // Register the AppID with Wechat library
        wechat_api = WXAPIFactory.createWXAPI(getContext(), MerchantDetail.getWechatPay_APP_ID(), true);
        wechat_api.registerApp(MerchantDetail.getWechatPay_APP_ID());
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPartnerSDK();
    }

    /**
     * Navigates to the webView page and proceed with the checkoutURL received
     * @param checkoutUrl the URL to be loaded in webview.
     */

    private void showWebView(String checkoutUrl) {
        Log.d(TAG, "showWebView with url:"+ checkoutUrl);
        Bundle bundle = new Bundle();
        bundle.putString(INTENTEXTRA_CHECKOUT_URL, checkoutUrl);
        NavHostFragment.findNavController(FragmentDirectSale.this)
                .navigate(R.id.action_directsales_to_fragmentWebView, bundle);

    }


    /**
     * Customizable methods to handle the action to be performed when transaction result is returned.
     * Merchant should implement their own action
     */

    protected void resultError(String message) {
        super.showError(message);

        Bundle bundle = new Bundle();
        bundle.putString(INTENTEXTRA_KEYSTATUS, CALLBACK_FAIL);
        try {
            NavHostFragment.findNavController(FragmentDirectSale.this)
                .navigate(R.id.action_directsales_to_fragmentShowResult, bundle);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Navigation failed");
        }

    }


    protected void resultSuccess(String message) {
        super.showSuccess(message);

        Bundle bundle = new Bundle();
        bundle.putString(INTENTEXTRA_KEYSTATUS, CALLBACK_SUCCESS);
        try { NavHostFragment.findNavController(FragmentDirectSale.this)
                .navigate(R.id.action_directsales_to_fragmentShowResult, bundle);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Navigation failed");
        }
    }

    private void checkout_paramcheck(PaymentType pType){
        Log.d(TAG,"Checkout param check");
        try {
            BigDecimal amount = commonInputsFragment.getTotalAmount();
            String merchantRef = commonInputsFragment.getMerchantRef();
            ArrayList<LineItems> lineItems = commonInputsFragment.getItemList();

            if (amount.compareTo(BigDecimal.ZERO) <= 0 || lineItems.isEmpty() ){
                showError(getString(R.string.error_paramfail));
            } else {

                switch (pType) {
                    case ALIPAYHK:
                    case ALIPAY: {
                        checkout_Alipay(pType, amount, merchantRef, lineItems);
                        break;
                    }
                    case WECHAT: {
                        checkout_wechat(amount, merchantRef, lineItems);
                        break;
                    }
                }
            }

        } catch (Exception e){
            showError(getString(R.string.error_paramfail)+e.getMessage());
        }
    }

    private void checkout_Alipay(PaymentType pType, BigDecimal amount, String merchantRef, ArrayList<LineItems> lineItems ){
        Log.d(TAG,"payAlipay button click ");

        // construct request objects
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setMerchantId(Long.parseLong(MerchantDetail.getMerchantID()));
        orderRequest.setAmount(amount);
        orderRequest.setCurrency("HKD");
        orderRequest.setPaymentType(pType);
        orderRequest.setNotifyUrl("https://google.com");
        orderRequest.setMerchantReference(merchantRef);
        orderRequest.setPaymentTerminal(PaymentTerminal.SDK);
        orderRequest.setLineItems(lineItems);


        String requestString = new Gson().toJson(orderRequest);
        String signature = SignUtils.sign(requestString, MerchantDetail.getMerchantPrivateKey() ,true);

        OnlinePayRequest onlinePayRequest = new OnlinePayRequest();
        onlinePayRequest.setRequest(requestString);
        onlinePayRequest.setSignature(signature);

        Call<SaleResponse> call = RetrofitClient.getRetrofitService().paymentDirectSaleOrder(onlinePayRequest);
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(@NotNull Call<SaleResponse> call, @NotNull Response<SaleResponse> response) {

                try {
                    Log.i(TAG,"Payment response:"+response);

                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {
                        SaleResponse saleResponse = response.body();
                        final Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(getActivity());
                                final String result = alipay.pay(saleResponse.getAlipayData(), true);
                                Map<String, String> reconstructedUtilMap = Arrays.stream(result.split(";"))
                                        .map(s -> s.split("="))
                                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));
                                PayResult payResult = new PayResult(reconstructedUtilMap);
                                String resultStatus = payResult.getResultStatus();
                                Log.i(TAG,"resultStatus:"+resultStatus);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        // Handling of payment result:
                                        if (TextUtils.equals(resultStatus, "{9000}")) {
                                            resultSuccess( getString(R.string.directsales_alipay_success));
                                        } else {
                                            resultError(getString(R.string.directsales_alipay_fail));
                                        }
                                    }
                                });
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }else{
                        ResponseBody error =  response.errorBody();
                        showError(getString(R.string.error_api_not_sucessful));
                    }
                } catch (Exception e) {
                    showError(getString(R.string.error_api_exception));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SaleResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
            }
        });
    }

    public void checkout_wechat(BigDecimal amount, String merchantRef, ArrayList<LineItems> lineItems ){
        Log.d(TAG,"payWechat button click ");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setMerchantId(Long.parseLong(MerchantDetail.getMerchantID()));
        orderRequest.setAmount(amount);
        orderRequest.setCurrency("HKD");
        orderRequest.setPaymentType(PaymentType.WECHAT);
        orderRequest.setMerchantReference(merchantRef);
        orderRequest.setPaymentTerminal(terminalmode);
        orderRequest.setLineItems(lineItems);

        String requestString = new Gson().toJson(orderRequest);
        String signature = SignUtils.sign(requestString, MerchantDetail.getMerchantPrivateKey() ,true);

        OnlinePayRequest onlinePayRequest = new OnlinePayRequest();
        onlinePayRequest.setRequest(requestString);
        onlinePayRequest.setSignature(signature);

        Call<SaleResponse> call = RetrofitClient.getRetrofitService().paymentDirectSaleOrder(onlinePayRequest);
        call.enqueue(new Callback<SaleResponse>() {
            @Override
            public void onResponse(@NotNull Call<SaleResponse> call, @NotNull Response<SaleResponse> response) {

                try {
                    Log.i(TAG,"Payment response:"+response);

                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {
                        SaleResponse saleResponse = response.body();

                        if (terminalmode.equals(PaymentTerminal.SDK)) {
                            WechatOrderRes wechatOrder = saleResponse.getWechatOrder();

                            PayReq request = new PayReq();
                            request.appId = wechatOrder.getAppId();//應用ID
                            request.partnerId = wechatOrder.getPartnerId();//商戶號
                            request.prepayId = wechatOrder.getPrepayId();//預支付交易會話ID
                            request.packageValue = wechatOrder.getPackageValue();//擴充套件欄位
                            request.nonceStr = wechatOrder.getNonceStr();//隨機字串
                            request.timeStamp = wechatOrder.getTimeStamp();//時間戳
                            request.sign = wechatOrder.getSign();//簽名

                            boolean result = wechat_api.sendReq(request);
                            Log.d(TAG, "wechat API onResponse: " + result);
                            if (result==false){
                                resultError(getString(R.string.directsales_wechatpay_fail));
                            }

                        } else if (terminalmode.equals(PaymentTerminal.WAP)){

                            String wechatOrderURL = saleResponse.getCheckoutUrl();
                            if (!StringUtil.isEmpty(wechatOrderURL)) {
                                Log.i(TAG,"CheckoutURL:" + wechatOrderURL);
                                if (URLUtil.isValidUrl(wechatOrderURL)) {
                                    showWebView(wechatOrderURL);
                                } else {
                                    showError(getString(R.string.error_api_invalid_url));
                                }
                            }else{
                                showError(getString(R.string.error_api_obj_null));
                            }
                        }

                    }else{
                        ResponseBody error =  response.errorBody();
                        showError(getString(R.string.error_api_not_sucessful));
                    }
                } catch (Exception e) {
                    showError(getString(R.string.error_api_exception));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SaleResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
            }
        });

    }

    /**
     * The methods below are not related to BBMSL Online Payment Gateway implementation
     */

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding =  FragmentDirectsaleBinding.inflate(inflater, container, false);
        binding.executePendingBindings();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPartnerSDK();

        binding.btnAlipayHK.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkout_paramcheck(PaymentType.ALIPAYHK);
                    }});
        binding.btnAlipayCN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                checkout_paramcheck(PaymentType.ALIPAY );
                    }});
        binding.btnWechat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkout_paramcheck(PaymentType.WECHAT);
                    }});
    }

    @Override
    public void onResume() {
        super.onResume();
        setupShoppingCartView();
    }

    protected void setupShoppingCartView() {
        commonInputsFragment = new FragmentCommonInputs();
        commonInputsFragment.setListener(FragmentDirectSale.this);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(binding.layoutCommonInputs.getId(), commonInputsFragment);
        fragmentTransaction.commit();

    }


}
