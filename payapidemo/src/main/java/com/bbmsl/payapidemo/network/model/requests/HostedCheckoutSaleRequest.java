package com.bbmsl.payapidemo.network.model.requests;

import com.bbmsl.payapidemo.network.model.dataobject.CallbackUrl;
import com.bbmsl.payapidemo.network.model.dataobject.LineItems;

import java.math.BigDecimal;
import java.util.ArrayList;

public class HostedCheckoutSaleRequest {

    String merchantId;
    BigDecimal amount;
    String merchantReference;
    CallbackUrl callbackUrl;
    boolean isRecurring	;
    String expiryTime;
    ArrayList<LineItems> lineItems;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public CallbackUrl getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(CallbackUrl url) {
        this.callbackUrl = url;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public ArrayList<LineItems> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<LineItems> lineitems) {
        this.lineItems = lineitems;
    }
}