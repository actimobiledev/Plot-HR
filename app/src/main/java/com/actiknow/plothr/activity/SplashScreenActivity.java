package com.actiknow.plothr.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.Transition;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.actiknow.plothr.R;
import com.actiknow.plothr.utils.AppConfigTags;
import com.actiknow.plothr.utils.AppConfigURL;
import com.actiknow.plothr.utils.Constants;
import com.actiknow.plothr.utils.EmployeePref;
import com.actiknow.plothr.utils.NetworkConnection;
import com.actiknow.plothr.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created by SUDHANSHU SHARMA on 19-02-2016.
 */
public class SplashScreenActivity extends AppCompatActivity {
    ProgressDialog progressDialogVolley;
    private static int SPLASH_TIME_OUT = 2000;
    CoordinatorLayout clMain;
    EmployeePref employeePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        initView();
        initData();

    }

    private void DelayPage() {
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                finish();
                // close this activity

            }
        }, SPLASH_TIME_OUT);
    }

    private void initData() {
        employeePref = EmployeePref.getInstance();

        progressDialogVolley = new ProgressDialog(this);
        if (NetworkConnection.isNetworkAvailable(SplashScreenActivity.this)) {
            //new CheckSubmitResponse().execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main));
            CheckSubmitResponse(String.valueOf(employeePref.getIntPref(SplashScreenActivity.this,EmployeePref.EMPLOYEE_ID)));
        }

    }

    private void initPreference() {
     /*   SharedPreferences prfs = getSharedPreferences(AppConfigTags.EMPLOYEE_DETAIL, Context.MODE_PRIVATE);
        com.actiknow.plothr.utils.Constants.employee_id_main = prfs.getInt("employee_id_main", 0);
        Log.d("Employee_id_main", "" + com.actiknow.plothr.utils.Constants.employee_id_main);*/


    }

    private void initView() {
         clMain = (CoordinatorLayout) findViewById(R.id.clMain);
    }

    private void CheckSubmitResponse(final String employee_id) {

        if (NetworkConnection.isNetworkAvailable(SplashScreenActivity.this)) {
            Utils.showProgressDialog(progressDialogVolley, "Please Wait", true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CHECKLOGIN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_CHECKLOGIN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    Constants.status = jsonObj.getInt(AppConfigTags.STATUS);
                                    switch (Constants.status) {
                                        case 0:
                                           // Toast.makeText(SplashScreenActivity.this, "Sorry you have already submitted your response", Toast.LENGTH_SHORT).show();
                                            Utils.showSnackBar (SplashScreenActivity.this, clMain, "Sorry you have already submitted your response", Snackbar.LENGTH_LONG, "sudhanshu", null);
                                            DelayPage();
                                           // finish();
                                            break;

                                        case 1:
                                            Intent intent1 = new Intent(SplashScreenActivity.this, SurveyActivity.class);
                                            startActivity(intent1);
                                            overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                            break;


                                        case 2:
                                            Intent intent2 = new Intent(SplashScreenActivity.this, MainActivity.class);
                                            startActivity(intent2);
                                            overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                            break;

                                    }
                                    /*if(Constants.status == 1){
                                        FragmentManager fragmentManager = getFragmentManager ();
                                        FragmentTransaction fragmentTransaction;
                                        fragmentTransaction = fragmentManager.beginTransaction ();
                                        com.actiknow.plothr.Fragment.LoginMobileFragment f2 = new com.actiknow.plothr.Fragment.LoginMobileFragment();

                                        // fragmentTransaction.setCustomAnimations (R.anim.fade_in, R.anim.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, f2, "fragment2");
                                        fragmentTransaction.commit();
                                    }else{
                                        Utils.showSnackBar (SplashScreenActivity.this, clMain, "Company Code is Incorrect", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    }
*/
                                    progressDialogVolley.dismiss();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss();
                                    Utils.showToast(SplashScreenActivity.this, getResources().getString(R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace();
                                }
                            } else {
                              //  Utils.showToast(SplashScreenActivity.this, getResources().getString(R.string.snackbar_text_error_occurred), true);
                                Utils.showSnackBar (SplashScreenActivity.this, clMain,  getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG,  getResources().getString(R.string.snackbar_text_error_occurred), null);

                                // Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialogVolley.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialogVolley.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                           // Utils.showToast(SplashScreenActivity.this, getResources().getString(R.string.snackbar_text_error_occurred), true);
                            Utils.showSnackBar (SplashScreenActivity.this, clMain,  getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG,  getResources().getString(R.string.snackbar_text_error_occurred), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.EMPLOYEE_ID, employee_id);
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

              /*  @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }*/
            };
            Utils.sendRequest(strRequest1, 60);
        } else {

        }


    }



   /* private class CheckSubmitResponse extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String employee_id = arg[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("employee_id", employee_id));
            Log.d("employee_id", employee_id);
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + com.actiknow.plothr.utils.AppConfigURL.URL_CHECKSUBMITRESPONSE);
            String json = serviceClient.makeServiceCall(com.actiknow.plothr.utils.AppConfigURL.URL_CHECKSUBMITRESPONSE, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArraySubmitResponse = jsonObj.getJSONArray("details");
                    Log.d("json array", "" + jsonArraySubmitResponse);
                    for (int i = 0; i < jsonArraySubmitResponse.length(); i++) {
                        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                        JSONObject c = jsonArraySubmitResponse.getJSONObject(i);
                        status = c.getInt("status");
                        map2.put("status", status);
                        Log.d("status", "" + status);
                        arrayListSubmitResponse.add(map2);
                    }
                } catch (JSONException e) {
                    Log.d("catch", "in the catch");
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
         //   progressDialog.dismiss();
            if ((status == 1)) {
                Intent intent1 = new Intent(SplashScreenActivity.this, SurveyActivity.class);
                startActivity(intent1);
            }    else if ((status == 2)) {
                Intent intent2 = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent2);
            } else if ((status == 0)) {
                Toast.makeText(SplashScreenActivity.this, "Sorry you have already submitted your response", Toast.LENGTH_SHORT).show();
            }
                finish();
        }
    }*/
   @Override
   public void onPause(){
       super.onPause();
       // put your code here...

   }
}
