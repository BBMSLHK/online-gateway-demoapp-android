package com.bbmsl.payapidemo.network.model.requests;


import com.bbmsl.payapidemo.network.model.dataobject.LineItems;
import com.bbmsl.payapidemo.constants.enums.PaymentTerminal;
import com.bbmsl.payapidemo.constants.enums.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderRequest  {

	Long merchantId;
	BigDecimal amount;
	String merchantReference;
	String currency;
	String notifyUrl;
	PaymentType paymentType;
	String token;
	PaymentTerminal paymentTerminal;
	ArrayList<LineItems> lineItems;


	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public ArrayList<LineItems> getLineItems() {
		return lineItems;
	}

	public void setLineItems(ArrayList<LineItems> lineItems) {
		this.lineItems = lineItems;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String callbackUrl) {
		this.notifyUrl = callbackUrl;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public PaymentTerminal getPaymentTerminal() {
		return paymentTerminal;
	}

	public void setPaymentTerminal(PaymentTerminal paymentTerminal) {
		this.paymentTerminal = paymentTerminal;
	}
}
