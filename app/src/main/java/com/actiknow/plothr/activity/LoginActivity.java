package com.actiknow.plothr.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.actiknow.plothr.Fragment.LoginCompanyFragment;
import com.actiknow.plothr.R;
import com.actiknow.plothr.utils.AppConfigTags;
import com.actiknow.plothr.utils.EmployeePref;
import com.actiknow.plothr.utils.Utils;


/**
 * Created by SUDHANSHU SHARMA on 24-02-2016.
 */
public class LoginActivity  extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        displayFirebaseRegId ();
        Bundle bundle = new Bundle();
        bundle.putString("mobilenumber", com.actiknow.plothr.utils.Constants.mobile_number);
        bundle.putString("companyid", com.actiknow.plothr.utils.Constants.companyCode);
        bundle.putString("employeeid", com.actiknow.plothr.utils.Constants.companyID);
        bundle.putInt("number_status", com.actiknow.plothr.utils.Constants.status);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginCompanyFragment f1 = new LoginCompanyFragment();
        f1.setArguments (bundle);
        fragmentTransaction.add(R.id.fragment_container, f1, "fragment1");
        fragmentTransaction.commit();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected (item);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed ();
    }

    private void displayFirebaseRegId () {
        SharedPreferences prfs = getSharedPreferences(AppConfigTags.EMPLOYEE_DETAIL, Context.MODE_PRIVATE);
        String firebase_id = prfs.getString(AppConfigTags.FIREBASE_ID, "");
        Utils.showLog(Log.ERROR, "Firebase Reg ID:", "sud", true);
    }
}