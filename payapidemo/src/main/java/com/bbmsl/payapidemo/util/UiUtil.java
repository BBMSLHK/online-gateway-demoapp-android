package com.bbmsl.payapidemo.util;

import com.bbmsl.payapidemo.R;
import com.bbmsl.payapidemo.constants.enums.CardType;

public class UiUtil {
    public static int getCardImageId(CardType cardType){
        switch (cardType){
            case MASTERCARD:
            case ECMC:
            {
                return(R.drawable.logo_master);
            }
            case VISA:{
                return(R.drawable.logo_visa);
            }
            default:{
                return(R.drawable.logo_othercard);
            }
        }
    }
}
