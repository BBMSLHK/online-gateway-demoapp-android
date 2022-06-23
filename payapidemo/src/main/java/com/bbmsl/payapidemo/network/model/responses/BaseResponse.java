package com.bbmsl.payapidemo.network.model.responses;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    String responseCode;
    String message;

    public String getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }
}
