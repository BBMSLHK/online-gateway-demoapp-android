package com.bbmsl.payapidemo.constants.enums;

import org.jetbrains.annotations.NotNull;

/***
 *   Order State Table
 *   ============================================================
 *   | Current State	|	Transaction		|	New State		|
 *   +------------------+-------------------+-------------------+
 *   | OPEN				|	Pay Success		|	SUCCESS			|
 *   |					|-------------------+-------------------+
 *   |					|	Pay Fail		|	OPEN			|
 *   |					|-------------------+-------------------+
 *   |					|	Cancel Success  | 	CANCELLED		|
 *   |					|-------------------+-------------------+
 *   |					|	Cancel Fail		| 	OPEN			|
 *   +------------------+-------------------+-------------------+
 *   | SUCCESS			| VoidPay Success   |	VOIDED			|
 *   |					|-------------------+-------------------+
 *   | 					| VoidPay Fail	 	|	SUCCESS			|
 *   |					|-------------------+-------------------+
 *   | 					| Refund Success 	|	REFUNDED		|
 *   |					|-------------------+-------------------+
 *   | 					| Refund Fail	 	|	SUCCESS			|
 *   +------------------+-------------------+-------------------+
 *   | VOIDED			| No txn allowed	|					|
 *   | REFUNDED			| 					|					|
 *   | CANCELLED		|					|					|
 *   ============================================================
 *   
 *   Transaction State Table
 *   ============================================================
 *   | Current State	|	Transaction		|	New State		|
 *   +------------------+-------------------+-------------------+
 *   | OPEN				|	Pay Success		|	SUCCESS			|
 *   |					|-------------------+-------------------+
 *   |					|	Pay Declined	|	DECLINED		|
 *   |					|-------------------+-------------------+
 *   |					|	Pay Cancelled	|	CANCELLED		|
 *   |					|-------------------+-------------------+
 *   |					|	Pay Fail		|	FAILURE			|
 *   +------------------+-------------------+-------------------+
 *   | SUCCESS			| VoidPay Success   |	VOIDED			|
 *   |					|-------------------+-------------------+
 *   | 					| VoidPay Fail	 	|	SUCCESS			|
 *   |					|-------------------+-------------------+
 *   | 					| Refund Success 	|	REFUNDED		|
 *   |					|-------------------+-------------------+
 *   | 					| Refund Fail	 	|	SUCCESS			|
 *   +------------------+-------------------+-------------------+
 *   | VOIDED			| No txn allowed	|					|
 *   | REFUNDED			| No txn allowed	|					|
 *   | CANCELLED		|					|					|
 *   ============================================================
 * 
 */

public enum StatusEnum {
	NONE("NONE"),
	OPEN("OPEN"),
	SUCCESS("SUCCESS"),
	CANCELLED("CANCELLED"),	
	PROCESSING("PROCESSING"),
	FAILURE("FAILURE"),
	DECLINED("DECLINED"),
	VOIDED("VOIDED"),
	REFUNDED("REFUNDED"),
    PARTIALLY_REFUNDED("PARTIALLY_REFUNDED"),
	;
    private final String name;

    StatusEnum(String stringVal) {
        name=stringVal;
    }

    @NotNull
    @Override
    public String toString(){
        return name;
    }

    public static String getEnumByString(String code){
        for(StatusEnum e : StatusEnum.values()){
            if(e.name.equals(code)) return e.name();
        }
        return null;
    }
}
