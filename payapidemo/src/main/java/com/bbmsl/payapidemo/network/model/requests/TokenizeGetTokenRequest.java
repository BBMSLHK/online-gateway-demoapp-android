package com.bbmsl.payapidemo.network.model.requests;

import com.bbmsl.payapidemo.network.model.dataobject.CallbackUrl;

public class TokenizeGetTokenRequest {

    String merchantId;
    String userId;
    String reason;
    CallbackUrl callbackUrl;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CallbackUrl getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(CallbackUrl url) {
        callbackUrl = url;
    }
}
