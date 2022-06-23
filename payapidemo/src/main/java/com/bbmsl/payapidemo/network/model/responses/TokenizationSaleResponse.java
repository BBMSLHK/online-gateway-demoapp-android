package com.bbmsl.payapidemo.network.model.responses;

import com.bbmsl.payapidemo.network.model.dataobject.Order;
import com.bbmsl.payapidemo.network.model.dataobject.Transaction;

public class TokenizationSaleResponse extends BaseResponse {

    Order order;
    Transaction transaction;

    public Order getOrder() {
        return order;
    }
    public Transaction getTransaction() {
        return transaction;
    }
}
