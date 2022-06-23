package com.bbmsl.payapidemo.config;

public class SampleCallbackURL {


    /**
     * Merchant should set up callbackURL for custom handling mechanism.
     * see FragmentWebView for details.
     */

    // callback uri settings
    public static final String CALLBACK_SCHEME = "bbmsl";
    public static final String CALLBACK_HOST = "saleresult";
    public static final String CALLBACK_KEYSTATUS = "status";
    public static final String CALLBACKURL_BASE = String.format("%s://%s?%s=",CALLBACK_SCHEME,CALLBACK_HOST, CALLBACK_KEYSTATUS );

    public static final String CALLBACK_SUCCESS = "success";
    public static final String CALLBACK_FAIL = "fail";
    public static final String CALLBACK_CANCELLED = "cancelled";
    public static final String CALLBACK_NOTIFY = "notify";
    public static final String CALLBACK_RETURN = "return";

}
