package com.bbmsl.payapidemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil {

    public static String getPref(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    public static void setPref(Context context, String key, String value){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setPrefIfEmpty(Context context, String key, String value){
        if (StringUtil.isEmpty( PreferenceManager.getDefaultSharedPreferences(context).getString(key, ""))){
            setPref( context,  key,  value);
        }
    }


}
