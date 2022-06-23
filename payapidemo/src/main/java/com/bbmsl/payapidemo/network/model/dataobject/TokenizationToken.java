package com.bbmsl.payapidemo.network.model.dataobject;

import com.bbmsl.payapidemo.constants.enums.CardType;

public class TokenizationToken {
    private String maskedPan;
    private String userId;
    private int tokenId;
    private String cardBrand;
    private String cardSubBrand;
    private String expiryDate;

    public String getMaskedPan() {
        return maskedPan;
    }

    public String getUserId() {
        return userId;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public CardType getCardBrand() {
        try{
            return CardType.valueOf(cardBrand);
        } catch (IllegalArgumentException e){
            return CardType.UNKNOWN;
        }
    }

    public String getCardSubBrand() {
        return cardSubBrand;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
    public String getFormattedExpiryDate() {
        try {
            return String.format("%s/%s", expiryDate.substring(0, 4), expiryDate.substring(4));
        } catch (Exception e){
            return getExpiryDate();
        }
    }
}
