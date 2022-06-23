package com.bbmsl.payapidemo.ui;

import android.os.Bundle;
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

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.constants.MerchantDetail;
import com.bbmsl.payapidemo.databinding.FragmentHostedcheckoutBinding;
import com.bbmsl.payapidemo.network.RetrofitClient;
import com.bbmsl.payapidemo.network.model.dataobject.CallbackUrl;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;
import com.bbmsl.payapidemo.network.model.requests.HostedCheckoutSaleRequest;
import com.bbmsl.payapidemo.network.model.requests.OnlinePayRequest;
import com.bbmsl.payapidemo.network.model.responses.HostedCheckoutSaleResponse;
import com.bbmsl.payapidemo.ui.components.FragmentCommonInputs;
import com.bbmsl.payapidemo.ui.main.BaseFragment;
import com.bbmsl.payapidemo.util.SignUtils;
import com.bbmsl.payapidemo.util.StringUtil;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_CHECKOUT_URL;


public class FragmentHostedCheckout extends BaseFragment {
    private static final String TAG = "FragmentHostedCheckout";

    private FragmentHostedcheckoutBinding binding;

    /**
     * Navigates to the webView page and proceed with the checkoutURL received
     * @param checkoutUrl the URL to be loaded in webview.
     */

    private void showWebView(String checkoutUrl) {
        Log.i(TAG, "showWebView with url:"+ checkoutUrl);
        Bundle bundle = new Bundle();
        bundle.putString(INTENTEXTRA_CHECKOUT_URL, checkoutUrl);
        NavHostFragment.findNavController(FragmentHostedCheckout.this)
                .navigate(R.id.action_hostedcheckout_to_fragmentWebView, bundle);

    }


    /**
     * Hosted checkout has 2 steps:
     * 1. call sales API to submit the transaction detail and receive a checkoutURL
     * 2. Load the checkoutURL to proceed with the transaction.
     */

    private void checkout_paramcheck(){
        Log.d(TAG,"Checkout param check");
        try {
            BigDecimal amount = commonInputsFragment.getTotalAmount();
            String merchantRef = commonInputsFragment.getMerchantRef();
            ArrayList<LineItems> lineItems = commonInputsFragment.getItemList();

            if (amount.compareTo(BigDecimal.ZERO) <= 0 || lineItems.isEmpty() ){
                showError(getString(R.string.error_paramfail));
            } else {
                checkout_apicall(amount, merchantRef, lineItems);
            }
        } catch (Exception e){
            showError(getString(R.string.error_paramfail)+e.getMessage());
        }
    }

    /**
     * call the checkout API
     */
    private void checkout_apicall(BigDecimal amount, String merchantRef, ArrayList<LineItems> lineItems ){
        Log.d(TAG,"calls checkout API");
        HostedCheckoutSaleRequest saleRequest = new HostedCheckoutSaleRequest();

        // required params
        saleRequest.setMerchantId(MerchantDetail.getMerchantID());
        saleRequest.setAmount(amount);
        saleRequest.setMerchantReference(merchantRef);
        saleRequest.setLineItems(lineItems);

        //optional params
        saleRequest.setRecurring(false);
        saleRequest.setExpiryTime(StringUtil.iso8601Date(600));
        saleRequest.setCallbackUrl (new CallbackUrl());

        String requestString = new Gson().toJson(saleRequest);
        String signature = SignUtils.sign(requestString, MerchantDetail.getMerchantPrivateKey() ,true);

        OnlinePayRequest onlinePayRequest = new OnlinePayRequest();
        onlinePayRequest.setRequest(requestString);
        onlinePayRequest.setSignature(signature);

        Call<HostedCheckoutSaleResponse> call = RetrofitClient.getRetrofitService().HostedCheckoutCreate(onlinePayRequest);
        call.enqueue(new Callback<HostedCheckoutSaleResponse>() {
            @Override
            public void onResponse(@NotNull Call<HostedCheckoutSaleResponse> call, @NotNull Response<HostedCheckoutSaleResponse> response) {

                try {
                    Log.i(TAG,"Payment response:"+response);

                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {
                        HostedCheckoutSaleResponse saleResponse = response.body();

                        String checkoutUrl = saleResponse.getCheckoutUrl();

                        if (!StringUtil.isEmpty(checkoutUrl)) {
                            Log.i(TAG,"CheckoutURL:" + checkoutUrl);
                            if (URLUtil.isValidUrl(checkoutUrl)) {
                                showWebView(checkoutUrl);
                            } else {
                                showError(getString(R.string.error_api_invalid_url));
                            }
                        }else{
                            showError(getString(R.string.error_api_obj_null));
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
            public void onFailure(@NotNull Call<HostedCheckoutSaleResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
            }
        });
    }



    /**
     * The methods below are not related to BBMSL Online Payment Gateway implementation
     */

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentHostedcheckoutBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add confirm btn listener
        binding.btnCheckout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "btnConfirm webview onclick");
                        FragmentHostedCheckout.this.checkout_paramcheck();
                    }});
    }

    @Override
    public void onResume() {
        super.onResume();
        setupShoppingCartView();
    }

    protected void setupShoppingCartView() {
        commonInputsFragment = new FragmentCommonInputs();
        commonInputsFragment.setListener(FragmentHostedCheckout.this);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(binding.layoutCommonInputs.getId(), commonInputsFragment);
        fragmentTransaction.commit();

    }

}
