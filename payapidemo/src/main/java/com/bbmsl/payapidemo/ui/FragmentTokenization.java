package com.bbmsl.payapidemo.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.constants.BaseConstants;
import com.bbmsl.payapidemo.constants.MerchantDetail;
import com.bbmsl.payapidemo.databinding.FragmentTokenizationBinding;
import com.bbmsl.payapidemo.network.RetrofitClient;
import com.bbmsl.payapidemo.network.model.dataobject.CallbackUrl;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;
import com.bbmsl.payapidemo.network.model.dataobject.TokenizationToken;
import com.bbmsl.payapidemo.network.model.requests.OnlinePayRequest;
import com.bbmsl.payapidemo.network.model.requests.TokenizeGetTokenRequest;
import com.bbmsl.payapidemo.network.model.requests.TokenizeSaleRequest;
import com.bbmsl.payapidemo.network.model.responses.QueryTokenResponse;
import com.bbmsl.payapidemo.network.model.responses.TokenizationCreateResponse;
import com.bbmsl.payapidemo.network.model.responses.TokenizationSaleResponse;
import com.bbmsl.payapidemo.ui.components.CardListAdapter;
import com.bbmsl.payapidemo.ui.components.FragmentCommonInputs;
import com.bbmsl.payapidemo.ui.components.TokenizedCardRadioButton;
import com.bbmsl.payapidemo.ui.main.BaseFragment;
import com.bbmsl.payapidemo.util.PrefUtil;
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

import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_RETURN;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_SUCCESS;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACKURL_BASE;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_KEYSTATUS;
import static com.bbmsl.payapidemo.constants.BaseConstants.KEY_USER_ID;

public class FragmentTokenization extends BaseFragment {
    private static final String TAG = "FragmentTokenization";

    private FragmentTokenizationBinding binding;

    private ArrayList<TokenizationToken> tokenList = new ArrayList<>();
    private CardListAdapter cardListAdapter ;


    private void getToken_process(){
        Log.i(TAG,"getToken Process");
        TokenizeGetTokenRequest saleRequest = new TokenizeGetTokenRequest();

        // required params
        saleRequest.setMerchantId(MerchantDetail.getMerchantID());
        saleRequest.setUserId(PrefUtil.getPref(getContext(),KEY_USER_ID));

        //optional params
        saleRequest.setReason("Demo app");

        // set callback to 'return' so the webview would return to this page instead
        CallbackUrl returnCallback = new CallbackUrl();
        returnCallback.setSuccess(CALLBACKURL_BASE + CALLBACK_RETURN);
        returnCallback.setCancel(CALLBACKURL_BASE + CALLBACK_RETURN);
        returnCallback.setFail(CALLBACKURL_BASE + CALLBACK_RETURN);

        saleRequest.setCallbackUrl (returnCallback);

        String requestString = new Gson().toJson(saleRequest);
        String signature = SignUtils.sign(requestString, MerchantDetail.getMerchantPrivateKey() ,true);

        OnlinePayRequest onlinePayRequest = new OnlinePayRequest();
        onlinePayRequest.setRequest(requestString);
        onlinePayRequest.setSignature(signature);

        Call<TokenizationCreateResponse> call = RetrofitClient.getRetrofitService().TokenizeCreate(onlinePayRequest);
        call.enqueue(new Callback<TokenizationCreateResponse>() {
            @Override
            public void onResponse(@NotNull Call<TokenizationCreateResponse> call, @NotNull Response<TokenizationCreateResponse> response) {

                try {
                    Log.i(TAG,"GetToken response:"+response);

                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {
                        Log.i(TAG,"GetToken response success");

                        TokenizationCreateResponse saleResponse = response.body();

                        String settokenUrl = saleResponse.getData();
                        if (!StringUtil.isEmpty(settokenUrl)) {
                            Log.i(TAG,"settokenUrl:" + settokenUrl);
                            if (URLUtil.isValidUrl(settokenUrl)) {
                                showWebView(settokenUrl);
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
            public void onFailure(@NotNull Call<TokenizationCreateResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
            }
        });
    }

    private void queryToken_process() {
        Log.d(TAG,"queryToken Process");

        TokenizeGetTokenRequest saleRequest = new TokenizeGetTokenRequest();

        // required params
        saleRequest.setMerchantId(MerchantDetail.getMerchantID());
        saleRequest.setUserId(PrefUtil.getPref(getContext(),KEY_USER_ID));

        String requestString = new Gson().toJson(saleRequest);
        String signature = SignUtils.sign(requestString, MerchantDetail.getMerchantPrivateKey() ,true);

        OnlinePayRequest onlinePayRequest = new OnlinePayRequest();
        onlinePayRequest.setRequest(requestString);
        onlinePayRequest.setSignature(signature);

        Call<QueryTokenResponse> call = RetrofitClient.getRetrofitService().TokenizeQuery(onlinePayRequest);
        call.enqueue(new Callback<QueryTokenResponse>() {
            @Override
            public void onResponse(@NotNull Call<QueryTokenResponse> call, @NotNull Response<QueryTokenResponse> response) {

                try {
                    Log.i(TAG,"Payment response:"+response);
                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {
                        QueryTokenResponse saleResponse = response.body();
                        Log.i(TAG,"Payment body:"+response.body());

                        if (saleResponse.getList() != null){
                            tokenList = saleResponse.getList();
                        } else{
                            tokenList = new ArrayList<>();
                        }
                    }else{
                        ResponseBody error =  response.errorBody();
                        showError(getString(R.string.error_api_not_sucessful));
                    }
                    updateTokenList();

                } catch (Exception e) {
                    showError(getString(R.string.error_api_exception));
                    tokenList = new ArrayList<>();
                    updateTokenList();
                }
            }

            @Override
            public void onFailure(@NotNull Call<QueryTokenResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
                tokenList = new ArrayList<>();
                updateTokenList();
            }
        });
    }

    private void checkout_paramcheck(){
        Log.d(TAG,"Checkout param check");
        try {
            BigDecimal amount = commonInputsFragment.getTotalAmount();
            String merchantRef = commonInputsFragment.getMerchantRef();
            ArrayList<LineItems> lineItems = commonInputsFragment.getItemList();
            int selectedCard = binding.rbToken.getCheckedRadioButtonId();

            if (amount.compareTo(BigDecimal.ZERO) <= 0 || lineItems.isEmpty() ){
                showError(getString(R.string.error_paramfail));
            } else {
                if (selectedCard < 0) {
                    showError(getString(R.string.tokenization_error_nocard_selected));
                } else {
                    checkout_apicall(amount, merchantRef, lineItems, selectedCard);
                }
            }
        } catch (Exception e){
            showError(getString(R.string.error_paramfail)+e.getMessage());
        }
    }

    private void checkout_apicall(BigDecimal amount, String merchantRef, ArrayList<LineItems> lineItems, int token ){
        Log.d(TAG,"calls checkout API: selected Token= "+token);
        TokenizeSaleRequest saleRequest = new TokenizeSaleRequest();

        // required params
        saleRequest.setMerchantId(MerchantDetail.getMerchantID());
        saleRequest.setUserId(PrefUtil.getPref(getContext(),KEY_USER_ID));
        saleRequest.setTokenId(token);
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

        Call<TokenizationSaleResponse> call = RetrofitClient.getRetrofitService().TokenizeSale(onlinePayRequest);
        call.enqueue(new Callback<TokenizationSaleResponse>() {
            @Override
            public void onResponse(@NotNull Call<TokenizationSaleResponse> call, @NotNull Response<TokenizationSaleResponse> response) {

                try {
                    Log.i(TAG,"Payment response:"+response);

                    if (response.isSuccessful() && response.body().getResponseCode().equals("0000")) {

                        TokenizationSaleResponse saleResponse = response.body();
                        Log.i("Payment","Payment body:"+response.body());

                        if ( saleResponse.getTransaction() != null) {

                            String result_msg = saleResponse.getTransaction().toString();

                            new AlertDialog.Builder(getContext())
                                    .setTitle(getString(R.string.dialog_success))
                                    .setMessage(result_msg)
                                    .setNeutralButton(getString(R.string.dialog_ok), null)
                                    .show();

                            Bundle bundle = new Bundle();
                            bundle.putString(INTENTEXTRA_KEYSTATUS, CALLBACK_SUCCESS);
                            NavHostFragment.findNavController(FragmentTokenization.this)
                                    .navigate(R.id.action_tokenization_to_fragmentShowResult, bundle);

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
            public void onFailure(@NotNull Call<TokenizationSaleResponse> call, @NotNull Throwable t) {
                showError(getString(R.string.error_apifail));
            }
        });
    }

    /**
     * Navigates to the webView page and proceed with the checkoutURL received
     * @param checkoutUrl the URL to be loaded in webview.
     */

    private void showWebView(String checkoutUrl){
        Log.d(TAG, "showWebView with url:"+ checkoutUrl);
        Bundle bundle = new Bundle();
        bundle.putString(BaseConstants.INTENTEXTRA_CHECKOUT_URL, checkoutUrl);
        NavHostFragment.findNavController(FragmentTokenization.this)
                .navigate(R.id.action_tokenization_to_fragmentWebView, bundle);
    }



    /**
     * The methods below are not related to BBMSL Online Payment Gateway implementation
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentTokenizationBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize  userId
        binding.edittextUserId.setText(PrefUtil.getPref(getContext(), KEY_USER_ID));

        cardListAdapter = new CardListAdapter(this.getContext(), R.layout.adapter_tokenized_cards,tokenList);
        binding.cardList.setAdapter(cardListAdapter);

        // add btn listener
        binding.btnUpdateUserid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "btnGetToken onclick");
                        PrefUtil.setPref(getContext(), KEY_USER_ID, binding.edittextUserId.getText().toString());

                        queryToken_process();

                        new AlertDialog.Builder(getContext())
                                .setMessage(getString(R.string.setting_setting_saved))
                                .setNeutralButton(getString(R.string.dialog_ok), null)
                                .setCancelable(true)
                                .show();
                    }});

        binding.btnGetToken.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Log.i(TAG,"btnGetToken onclick");
                    FragmentTokenization.this.getToken_process();
                }});

        binding.btnCheckout.setOnClickListener(
                new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                    Log.i(TAG,"btnCheckout onclick");
                    showCardMenu();
                }});

        binding.btnConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Log.i(TAG,"btnConfirm onclick");
                    FragmentTokenization.this.checkout_paramcheck();
                }});


        // add onclick listener for closing the selectcard layout
        binding.cardSelectBackground.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(TAG, "cardSelectBackground onclick");
                        hideCardMenu();
                    }});

        queryToken_process();

    }

    @Override
    public void onResume() {
        super.onResume();
        setupShoppingCartView();
    }

    protected void setupShoppingCartView() {
        commonInputsFragment = new FragmentCommonInputs();
        commonInputsFragment.setListener(FragmentTokenization.this);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(binding.layoutCommonInputs.getId(), commonInputsFragment);
        fragmentTransaction.commit();

    }

    private void updateTokenList(){
        binding.rbToken.removeAllViews();
        cardListAdapter.clear();

        for (TokenizationToken card : tokenList) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);
            TokenizedCardRadioButton rb = new TokenizedCardRadioButton(getContext());
            rb.setLayoutParams(layoutParams);

            rb.setId(card.getTokenId());
            rb.setData(card.getMaskedPan(), card.getFormattedExpiryDate(), card.getCardBrand());
            binding.rbToken.addView(rb);

            cardListAdapter.add(card);
        }
        if (!tokenList.isEmpty()) {
            binding.rbToken.check(tokenList.get(0).getTokenId());
        }
        cardListAdapter.notifyDataSetChanged();

        if (tokenList.isEmpty()){
            binding.layoutShoppingCart.setAlpha(0.3f);
            binding.layoutCheckout.setAlpha(0.3f);
            binding.btnCheckout.setEnabled(false);
            commonInputsFragment.enable(false);
        }else {
            binding.layoutShoppingCart.setAlpha(1);
            binding.layoutCheckout.setAlpha(1);
            binding.btnCheckout.setEnabled(true);
            commonInputsFragment.enable(true);
        }
    }

    // slide the view from below itself to the current position
    public void showCardMenu(){
        Log.d(TAG, "showCardMenu");
        binding.cardSelectBackground.setVisibility(View.VISIBLE);
        binding.layoutCardSelect.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                binding.layoutCardSelect.getHeight(),  // fromYDelta
                0);                // toYDelta
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.layoutCardSelect.startAnimation(animation);
    }

    // slide the view from its current position to below itself
    public void hideCardMenu(){
        Log.d(TAG, "hideCardMenu");
        TranslateAnimation animation = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                binding.layoutCardSelect.getHeight()); // toYDelta
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardSelectBackground.setVisibility(View.GONE);
                binding.layoutCardSelect.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.layoutCardSelect.startAnimation(animation);

    }


}
