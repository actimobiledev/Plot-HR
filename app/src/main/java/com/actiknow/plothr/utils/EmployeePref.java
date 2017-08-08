package com.actiknow.plothr.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class EmployeePref {
    public static String COMPANY_ID = "company_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String MOBILE = "mobile";
    public static String OTP = "otp";
    public static String BY_PASS_OTP = "by_pass_otp";
    public static String BUYER_ID = "buyer_id";
    public static String BUYER_FIREBASE_ID = "buyer_firebase_id";
    public static String BUYER_IMAGE = "buyer_image";
    public static String BUYER_FACEBOOK_ID = "buyer_facebook_id";
    public static String BUYER_LINKEDIN_ID = "buyer_linkedin_id";
    public static String PROFILE_STATUS = "profile_status";
    public static String PROFILE_HOME_TYPE = "profile_home_type";
    public static String PROFILE_STATE = "profile_state";
    public static String PROFILE_PRICE_RANGE = "profile_price_range";
    private static EmployeePref employeePref;
    private String EMPLOYEE = "EMPLOYEE";
    
    public static EmployeePref getInstance () {
        if (employeePref == null)
            employeePref = new EmployeePref ();
        return employeePref;
    }

    private SharedPreferences getPref (Context context) {
        return context.getSharedPreferences (EMPLOYEE, Context.MODE_PRIVATE);
    }

    public String getStringPref (Context context, String key) {
        return getPref (context).getString (key, "");
    }

    public int getIntPref (Context context, String key) {
        return getPref (context).getInt (key, 0);
    }

    public boolean getBooleanPref (Context context, String key) {
        return getPref (context).getBoolean (key, false);
    }

    public void putBooleanPref (Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putBoolean (key, value);
        editor.apply ();
    }

    public void putStringPref (Context context, String key, String value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putString (key, value);
        editor.apply ();
    }

    public void putIntPref (Context context, String key, int value) {
        SharedPreferences.Editor editor = getPref (context).edit ();
        editor.putInt (key, value);
        editor.apply ();
    }
}
