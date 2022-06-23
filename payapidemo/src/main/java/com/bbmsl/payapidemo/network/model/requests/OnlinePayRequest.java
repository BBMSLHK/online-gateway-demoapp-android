package com.bbmsl.payapidemo.network.model.requests;


public class OnlinePayRequest {

	String request;
	String signature;

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
