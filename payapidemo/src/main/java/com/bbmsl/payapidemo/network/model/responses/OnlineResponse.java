package com.bbmsl.payapidemo.network.model.responses;

import org.jetbrains.annotations.NotNull;

public class OnlineResponse extends BaseResponse{

	String response;
	String signature;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@NotNull
    @Override
	public String toString() {
		return "response={" + response + "};signature={" + signature
				+ "}";
	}
}
