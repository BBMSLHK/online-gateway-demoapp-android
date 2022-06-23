package com.bbmsl.payapidemo.network.model.dataobject;

import java.math.BigDecimal;


public class LineItems {

    private int quantity;
    private PriceData priceData;

    public LineItems(){
        this.quantity = 1;
        this.priceData = new PriceData( "Something",new BigDecimal(0));
    }
    public LineItems( String name, BigDecimal unitAmount, int quantity) {
        this.quantity = quantity;
        this.priceData = new PriceData(name, unitAmount);
    }

    public int getQuantity() {
        return quantity;
    }
    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriceUnitAmount(BigDecimal unitAmount) {
        this.priceData.setUnitAmount (unitAmount);
    }
    public void setPriceName(String name) {
        this.priceData.setName(name);
    }

    public static class PriceData{
        //String currency;
        private String name;
        private BigDecimal unitAmount;

        public PriceData( String name,BigDecimal unitAmount) {
            this.name = name;
            this.unitAmount = unitAmount;
        }

        public String getName() {
            return name;
        }
        public BigDecimal getUnitAmount() {
            return unitAmount;
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setUnitAmount(BigDecimal unitAmount) {
            this.unitAmount = unitAmount;
        }

    }
}
