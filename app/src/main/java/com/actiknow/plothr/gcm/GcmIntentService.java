package com.actiknow.plothr.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.actiknow.plothr.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName ();

    public GcmIntentService () {
        super (TAG);
    }

    public static final String KEY = "key";


    @Override
    protected void onHandleIntent (Intent intent) {
        String key = intent.getStringExtra (KEY);
        Log.d(TAG, "in gcm intent service");

        switch (key) {
            default:
                registerGCM ();
        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;
       Log.d(TAG, "in register gcm");

        int appVersion = com.actiknow.plothr.utils.Utils.getAppVersion(this);
        try {
            InstanceID instanceID = InstanceID.getInstance (this);
          //  token = instanceID.getToken (getString (R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Token: " + token);
            //    Toast.makeText(this, "GCM Token : " + token, Toast.LENGTH_SHORT).show();
            // sending the registration id to our server
            //sendRegistrationToServer (token);
            com.actiknow.plothr.utils.Constants.regid = token;
//            UserDetailsPrefData userDetailsPrefData = UserDetailsPrefData.getInstance ();
//            userDetailsPrefData.putStringPref (this, Constants.REGISTRATION_ID, token);
//            userDetailsPrefData.putStringPref (this, Constants.APP_VERSION, String.valueOf(appVersion));

//            sharedPreferences.edit ().putBoolean (Config.SENT_TOKEN_TO_SERVER, true).apply ();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

//            sharedPreferences.edit ().putBoolean (Config.SENT_TOKEN_TO_SERVER, false).apply ();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(com.actiknow.plothr.utils.Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra ("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast (registrationComplete);
    }
 }