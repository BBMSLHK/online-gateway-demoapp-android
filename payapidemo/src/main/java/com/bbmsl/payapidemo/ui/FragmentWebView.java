package com.bbmsl.payapidemo.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.databinding.FragmentWebviewBinding;
import com.bbmsl.payapidemo.ui.main.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_HOST;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_KEYSTATUS;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_RETURN;
import static com.bbmsl.payapidemo.config.SampleCallbackURL.CALLBACK_SCHEME;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_CHECKOUT_URL;
import static com.bbmsl.payapidemo.constants.BaseConstants.INTENTEXTRA_KEYSTATUS;
import static com.bbmsl.payapidemo.constants.HostAddress.MSL_HOME;

public class FragmentWebView extends BaseFragment {

    private static final String TAG = "WebViewActivity";

    FragmentWebviewBinding binding;
    private Context context ;

    private String checkoutUrl = "";
    private final ArrayList<WebView> popupStack = new ArrayList<>();

    HashMap <String, String> extraHeaders = new HashMap<>();

    /**
     * Customizable method to handle the action to be performed when Callback URL is detected.
     * In this demo, it reads the URL of the callback and decides what action to be performed.
     * Merchant should implement their own action
     * @param url The callback URL submitted at API call
     */
    private void handleCallbackUrl(Uri url) {
        Log.d(TAG, "Callback detected: Uri="+url.toString());

        // should close the webview before redirection
        closeWebview();

        // Custom handling: get back to last page if status is "return";
        // this is for the add Token action of the Tokenization flow
        if (url.getQueryParameter(CALLBACK_KEYSTATUS).equals(CALLBACK_RETURN)){
            NavHostFragment.findNavController(FragmentWebView.this)
                    .popBackStack();
        } else {
            // Checkout flow navigates the user to the result page
            Bundle bundle = new Bundle();
            bundle.putString(INTENTEXTRA_KEYSTATUS, url.getQueryParameter(CALLBACK_KEYSTATUS));
            NavHostFragment.findNavController(FragmentWebView.this)
                    .navigate(R.id.action_fragmentWebView_to_fragmentShowResult, bundle);
        }
    }

    /**
     * PDF handle. The T&C document is in PDF format.
     * In this demo it is to open with an external App.
     */
    private void handlePDFDownload(Uri url) {
        Log.d(TAG, "handlePDFDownload: Uri="+url.toString());

        try {
            closeAllPopupView();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e){
            showError(getString(R.string.error_no_pdf_handle));
        }

    }

    /**
     *  Initialization. The URL is get from Fragment Argument. Then initialize the webview and load it.
     *  Initialize the header required by using WeChat Pay checkout.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    /* */
        if (getArguments().containsKey(INTENTEXTRA_CHECKOUT_URL)) {
            checkoutUrl = getArguments().getString(INTENTEXTRA_CHECKOUT_URL);
        } else{
            checkoutUrl = MSL_HOME;
        }

        /* put extra headers required by Wechat */
        extraHeaders.put("Referer","https://checkout.dev.bbmsl.com/");

        context = getContext();
        showWebView(checkoutUrl);
    }

    /**
     * Override back button behaviour
     * To redirect user back to select payment page when back button is pressed.
     */
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

       getActivity().getOnBackPressedDispatcher().addCallback(this,
        new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "onBackPressed");
                if (popupStack.size()>0){
                    closeAllPopupView();
                }

                if (binding.theWebView.canGoBack()) {
                    Log.d(TAG, "theWebView goBack(), stack size= "+binding.theWebView.copyBackForwardList().getSize());
                    binding.theWebView.loadUrl(checkoutUrl, extraHeaders);
                    binding.theWebView.clearHistory();

                } else {
                    Log.d(TAG, "Quit webview");
                    closeWebview();
                    NavHostFragment.findNavController(FragmentWebView.this).popBackStack();
                }
            }
        });
    }

    /**
     * Initialize the webview.
     * @param url the URL to be loaded in webview.
     */

    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView(String url){
        Log.d(TAG, String.format ("showWebView: %s",url) );

        binding.theWebView.getSettings().setDomStorageEnabled(true);
        binding.theWebView.getSettings().setJavaScriptEnabled(true);
        binding.theWebView.getSettings().setSupportMultipleWindows(true); // for popup gPay windows
        binding.theWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // for popup wechat pay

        binding.theWebView.setWebChromeClient(
                new WebChromeClient() {

                    /** Override onCreate to handle popup for Google Pay */
                    @Override
                    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                        Log.d(TAG, "webview onCreateWindow");

                        showPopup(resultMsg);
                        return true;
                    }

                    @Override
                    public void onProgressChanged(WebView view, int progress)
                    {
                        super.onProgressChanged(view, progress);
                        binding.txtTitle.setText(getString(R.string.webview_loading));
                        binding.progressBar.setProgress(progress);
                        binding.progressBar.setVisibility( (progress >= 100)? View.GONE : View.VISIBLE);
                    }

                });

        binding.theWebView.setWebViewClient(
                new WebViewClient() {

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        binding.txtTitle.setText(view.getTitle());
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        Log.d(TAG, "theWebView shouldOverrideUrlLoading:" + request.getUrl());

                        String urlScheme = request.getUrl().getScheme();
                        String urlHost = request.getUrl().getHost();

                        if (urlScheme.equals("https") || urlScheme.equals("http")) {
                            Log.d(TAG, "HTTPS handled");

                            if (request.getUrl().toString().endsWith(".pdf")) {
                                Log.d(TAG, "PDF detected ");
                                handlePDFDownload(request.getUrl());
                            }else {
                                Log.d(TAG, "Load the url normally.");
                                view.loadUrl(request.getUrl().toString(), extraHeaders);
                            }
                        } else if (urlScheme.equals(CALLBACK_SCHEME) && urlHost.equals(CALLBACK_HOST)){
                            Log.d(TAG, "Callback detected");

                            handleCallbackUrl(request.getUrl());

                        }else{
                            Log.d(TAG, "Other intent handled, i.e. Wechat app deeplink");

                            Intent defaultIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                            if (defaultIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(defaultIntent);
                            } else {

                                new AlertDialog.Builder(context)
                                        .setTitle(getString(R.string.dialog_error))
                                        .setMessage(getString(R.string.webview_app_not_installed))
                                        .setNeutralButton(getString(R.string.dialog_ok), null)
                                        .setCancelable(true)
                                        .show();
                            }
                        }
                        return true;
                    }
                }
        );

        binding.theWebView.loadUrl(url, extraHeaders);

    }

    /** Show popup
     * for Google Pay, a popup is required to proceed the payment process.
     * in this example, the popup will be loaded to a new webview and attach to the layout.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void showPopup (Message resultMsg){
        Log.d(TAG, "showPopup");

        // setup popup
        WebView popupView = new WebView(context);
        popupView.getSettings().setDomStorageEnabled(true);
        popupView.getSettings().setJavaScriptEnabled(true);

        popupView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "theWebView shouldOverrideUrlLoading:" + request.getUrl());

                if (request.getUrl().toString().endsWith(".pdf")) {
                    Log.d(TAG, "PDF detected. ");
                    handlePDFDownload(request.getUrl());
                }else {
                    view.loadUrl(request.getUrl().toString(), extraHeaders);
                }
                return true;
            }

        });

        popupView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(WebView window) {
                Log.d(TAG, "Popup onCloseWindow");
                closePopupView(popupView);
                super.onCloseWindow(window);
            }
        });

        popupStack.add(popupView);

        binding.webContainer.addView(popupView);
        binding.webContainer.setVisibility(View.VISIBLE);

        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(popupView);
        resultMsg.sendToTarget();
    }

    /**
     * The following methods are for closing the popups or main webviews.
     */

    private void closeWebview() {
        Log.d(TAG, "closeWebview");
        binding.theWebView.loadUrl("about:blank");
        binding.theWebView.clearHistory();
    }

    private void closePopupView(WebView popup) {
        Log.d(TAG, "closePopupView");

        if (popup != null) {
            popupStack.remove(popup);
            popup.clearHistory();
            binding.webContainer.removeView(popup);

            if (popupStack.size() <= 0) {
                binding.webContainer.setVisibility(View.GONE);
            }
        }
    }

    private void closeAllPopupView() {
        Log.d(TAG, String.format("closeAllPopupView, popupStack size = %d", popupStack.size()));

        try {
            // unload and remove all popups in popup stack
            while (!popupStack.isEmpty()) {
                closePopupView(popupStack.get(0));
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        binding.webContainer.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =  FragmentWebviewBinding.inflate(inflater, container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

}
