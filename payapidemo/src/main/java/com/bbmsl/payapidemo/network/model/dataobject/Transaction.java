package com.bbmsl.payapidemo.network.model.dataobject;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Transaction {

    private int  id;
    private int  merchantId;
    private String  type;
    private String  amount;
    private String  status;
    private String  maskedPan;
    private String  stan;

    public int getId() {
        return id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public String getStan() {
        return stan;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return String.format("Transaction Result: \nid=%s \nmerchantId=%s \ntype=%s \namount=%s \nstatus=%s \nmaskedPan=%s \nstan=%s ",
          id,
          merchantId,
          type,
          amount,
          status,
          maskedPan,
          stan );
    }
}
