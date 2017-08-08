package com.actiknow.plothr.app;

import android.app.Application;



public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName ();

    private static MyApplication mInstance;

    private com.actiknow.plothr.helper.MyPreferenceManager pref;

    @Override
    public void onCreate () {
        super.onCreate ();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance () {
        return mInstance;
    }


    public com.actiknow.plothr.helper.MyPreferenceManager getPrefManager () {
        if (pref == null) {
            pref = new com.actiknow.plothr.helper.MyPreferenceManager(this);
        }
        return pref;
    }
}