package com.bbmsl.payapidemo.network.model.responses;

import com.bbmsl.payapidemo.network.model.dataobject.Order;

public class HostedCheckoutSaleResponse extends BaseResponse {

    String checkoutUrl;
    Order order;

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
    public Order getOrder() {
        return order;
    }

}
