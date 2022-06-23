package com.bbmsl.payapidemo.constants;

import com.bbmsl.payapidemo.util.StringUtil;

public class MerchantDetail {

    // Default Settings
    private static String merchantID = "";
    private static String merchantPrivateKey = "";
    private static String WechatPay_APP_ID = "";
    // keys


    public static String getMerchantID() {
        return merchantID;
    }

    public static void setMerchantID(String themerchantID) {
        merchantID = themerchantID;
    }

    public static String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public static void setMerchantPrivateKey(String themerchantPrivateKey) {
        merchantPrivateKey = themerchantPrivateKey;
    }

    public static String getWechatPay_APP_ID() {
        return WechatPay_APP_ID;
    }

    public static void setWechatPay_APP_ID(String wechatPay_APP_ID) {
        WechatPay_APP_ID = wechatPay_APP_ID;
    }

    public static boolean notComplete (){
        return   ( StringUtil.isEmpty(merchantID)
                || StringUtil.isEmpty(merchantPrivateKey)
//                || StringUtil.isEmpty(WechatPay_APP_ID)  /*optional*/
        );
    }
}
