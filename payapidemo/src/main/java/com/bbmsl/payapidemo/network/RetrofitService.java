package com.bbmsl.payapidemo.network;

import com.bbmsl.payapidemo.network.model.requests.OnlinePayRequest;
import com.bbmsl.payapidemo.network.model.responses.SaleResponse;
import com.bbmsl.payapidemo.network.model.responses.HostedCheckoutSaleResponse;
import com.bbmsl.payapidemo.network.model.responses.QueryTokenResponse;
import com.bbmsl.payapidemo.network.model.responses.TokenizationCreateResponse;
import com.bbmsl.payapidemo.network.model.responses.TokenizationSaleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {

    // Hosted checkout Api
    @POST("/hosted-checkout/create")
    Call<HostedCheckoutSaleResponse> HostedCheckoutCreate(@Body OnlinePayRequest onlinePayRequest);

   // Tokenization api
    @POST("/tokenization/add-token")
    Call<TokenizationCreateResponse> TokenizeCreate(@Body OnlinePayRequest onlinePayRequest);

    @POST("tokenization/query-token")
    Call<QueryTokenResponse> TokenizeQuery(@Body OnlinePayRequest onlinePayRequest);

    @POST("/tokenization/sale")
    Call<TokenizationSaleResponse> TokenizeSale(@Body OnlinePayRequest onlinePayRequest);

    // Direct sales Api
    @POST("/direct/sale")
    Call<SaleResponse> paymentDirectSaleOrder(@Body OnlinePayRequest onlinePayRequest);

}
