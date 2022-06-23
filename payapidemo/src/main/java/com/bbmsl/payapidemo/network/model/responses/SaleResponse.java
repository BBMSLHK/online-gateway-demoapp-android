package com.bbmsl.payapidemo.network.model.responses;

import com.bbmsl.payapidemo.network.model.directsales.WechatOrderRes;

public class SaleResponse extends BaseResponse{

    WechatOrderRes wechatOrder;
    String alipayData;
    String checkoutUrl;

    public WechatOrderRes getWechatOrder() {
        return wechatOrder;
    }
    public String getAlipayData() {
        return alipayData;
    }
    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}
