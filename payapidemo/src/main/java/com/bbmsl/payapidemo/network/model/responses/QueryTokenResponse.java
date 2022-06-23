package com.bbmsl.payapidemo.network.model.responses;

import com.bbmsl.payapidemo.network.model.dataobject.TokenizationToken;

import java.util.ArrayList;

public class QueryTokenResponse extends BaseResponse {

    ArrayList<TokenizationToken> list;

    public ArrayList<TokenizationToken> getList() {
        return list;
    }

}
