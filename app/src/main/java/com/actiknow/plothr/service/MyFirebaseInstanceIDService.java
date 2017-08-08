package com.actiknow.plothr.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.actiknow.plothr.utils.AppConfigTags;
import com.actiknow.plothr.utils.Constants;
import com.actiknow.plothr.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName ();
    @Override
    public void onTokenRefresh () {
        super.onTokenRefresh ();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.showLog (Log.DEBUG, TAG, "Refreshed Token: " + refreshedToken, true);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences (AppConfigTags.EMPLOYEE_DETAIL, Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putString(AppConfigTags.FIREBASE_ID, refreshedToken);
        Log.d(AppConfigTags.FIREBASE_ID,refreshedToken);
        editor.apply();
    
       // BuyerDetailsPref buyerDetailsPref = BuyerDetailsPref.getInstance ();
        //buyerDetailsPref.putStringPref (getApplicationContext (), BuyerDetailsPref.BUYER_FIREBASE_ID, refreshedToken);
    }
}

