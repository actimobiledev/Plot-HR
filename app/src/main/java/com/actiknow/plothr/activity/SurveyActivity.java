package com.actiknow.plothr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static android.os.Build.VERSION_CODES.M;
import static com.actiknow.plothr.activity.ServiceHandler.response;


/**
 * Created by SUDHANSHU SHARMA on 25-02-2016.
 */
public class SurveyActivity extends Activity implements View.OnClickListener {

    static int employee_id_main = 0;
    static int status = 0;
    ProgressDialog progressDialog;
    int responsebenefits = 0;
    int responsecareer = 0;
    int responsemanagement = 0;
    int responsesuccess = 0;
    int responsecommunication = 0;
    int responseteam = 0;
    JSONArray jsonArraySubmitResponse = null;
    ArrayList<HashMap<String, Integer>> arrayListSubmitResponse = new ArrayList<HashMap<String, Integer>>();
    ImageView tvbenefitshappy;
    ImageView tvbenefitsneutral;
    ImageView tvbenefitssad;
    ImageView tvcommunicationhappy;
    ImageView tvcommunicationneutral;
    ImageView tvcommunicationsad;
    ImageView tvteamhappy;
    ImageView tvteamneutral;
    ImageView tvteamsad;
    ImageView tvcareerhappy;
    ImageView tvcareerneutral;
    ImageView tvcareersad;
    ImageView tvmanagementhappy;
    ImageView tvmanagementneutral;
    ImageView tvmanagementsad;
    ImageView tvsuccesshappy;
    ImageView tvsuccessneutral;
    ImageView tvsuccesssad;

    EditText etcomment;
    String comment;
    Button btsubmit;
    SharedPreferences prfs;
    ProgressDialog progressDialogVolley;
    EmployeePref employeePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);
        initView();
        initData();
        initListener();

        Intent intent = getIntent();
        prfs = getSharedPreferences("EMPLOYEE_DETAIL", Context.MODE_PRIVATE);
        com.actiknow.plothr.utils.Constants.employee_id_main = prfs.getInt("employee_id_main", 0);
        Log.d("Employee_id_main", "" + com.actiknow.plothr.utils.Constants.employee_id_main);


       // btsubmit = (Button) findViewById(R.id.button4);



    }

    private void initView() {
        tvbenefitshappy = (ImageView) findViewById(R.id.tvbenefitshappy);
        tvbenefitsneutral = (ImageView) findViewById(R.id.tvbenefitsneutral);
        tvbenefitssad = (ImageView) findViewById(R.id.tvbenefitssad);
        tvcommunicationhappy = (ImageView) findViewById(R.id.tvcommunicationhappy);
        tvcommunicationneutral = (ImageView) findViewById(R.id.tvcommunicationneutral);
        tvcommunicationsad = (ImageView) findViewById(R.id.tvcommunicationsad);
        tvteamhappy = (ImageView) findViewById(R.id.tvteamhappy);
        tvteamneutral = (ImageView) findViewById(R.id.tvteamneutral);
        tvteamsad = (ImageView) findViewById(R.id.tvteamsad);
        tvcareerhappy = (ImageView) findViewById(R.id.tvcareerhappy);
        tvcareerneutral = (ImageView) findViewById(R.id.tvcareerneutral);
        tvcareersad = (ImageView) findViewById(R.id.tvcareersad);
        tvmanagementhappy = (ImageView) findViewById(R.id.tvmanagementhappy);
        tvmanagementneutral = (ImageView) findViewById(R.id.tvmanagementneutral);
        tvmanagementsad = (ImageView) findViewById(R.id.tvmanagementsad);
        tvsuccesshappy = (ImageView) findViewById(R.id.tvsuccesshappy);
        tvsuccessneutral = (ImageView) findViewById(R.id.tvsuccessneutral);
        tvsuccesssad = (ImageView) findViewById(R.id.tvsuccesssad);
        etcomment = (EditText)findViewById(R.id.etcomment);

        btsubmit = (Button) findViewById(R.id.button4);
    }

    private void initData(){
        progressDialogVolley = new ProgressDialog(this);
        employeePref = EmployeePref.getInstance();
    }

    private void initListener(){
        tvbenefitshappy.setOnClickListener(this);
        tvbenefitsneutral.setOnClickListener(this);
        tvbenefitssad.setOnClickListener(this);
        tvcommunicationhappy.setOnClickListener(this);
        tvcommunicationneutral.setOnClickListener(this);
        tvcommunicationsad.setOnClickListener(this);
        tvteamhappy.setOnClickListener(this);
        tvteamneutral.setOnClickListener(this);
        tvteamsad.setOnClickListener(this);
        tvcareerhappy.setOnClickListener(this);
        tvcareerneutral.setOnClickListener(this);
        tvcareersad.setOnClickListener(this);
        tvmanagementhappy.setOnClickListener(this);
        tvmanagementneutral.setOnClickListener(this);
        tvmanagementsad.setOnClickListener(this);
        tvsuccesshappy.setOnClickListener(this);
        tvsuccessneutral.setOnClickListener(this);
        tvsuccesssad.setOnClickListener(this);
        btsubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        comment = (etcomment).getText().toString();
    //   btsubmit = (Button) findViewById(R.id.button4);
        switch (v.getId()) {

            case R.id.tvbenefitshappy:
                responsebenefits = 1;
                tvbenefitshappy.setImageResource(R.drawable.ic_happyselected);
                tvbenefitsneutral.setImageResource(R.drawable.ic_neutral);
                tvbenefitssad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvbenefitsneutral:
                responsebenefits = 2;
                tvbenefitshappy.setImageResource(R.drawable.ic_happy);
                tvbenefitsneutral.setImageResource(R.drawable.ic_neutralselected);
                tvbenefitssad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvbenefitssad:
                responsebenefits = 3;
                tvbenefitshappy.setImageResource(R.drawable.ic_happy);
                tvbenefitsneutral.setImageResource(R.drawable.ic_neutral);
                tvbenefitssad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.tvcommunicationhappy:
                responsecommunication = 1;
                tvcommunicationhappy.setImageResource(R.drawable.ic_happyselected);
                tvcommunicationneutral.setImageResource(R.drawable.ic_neutral);
                tvcommunicationsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvcommunicationneutral:
                responsecommunication = 2;
                tvcommunicationhappy.setImageResource(R.drawable.ic_happy);
                tvcommunicationneutral.setImageResource(R.drawable.ic_neutralselected);
                tvcommunicationsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvcommunicationsad:
                responsecommunication = 3;
                tvcommunicationhappy.setImageResource(R.drawable.ic_happy);
                tvcommunicationneutral.setImageResource(R.drawable.ic_neutral);
                tvcommunicationsad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.tvteamhappy:
                responseteam = 1;

                tvteamhappy.setImageResource(R.drawable.ic_happyselected);
                tvteamneutral.setImageResource(R.drawable.ic_neutral);
                tvteamsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvteamneutral:
                responseteam = 2;
                tvteamhappy.setImageResource(R.drawable.ic_happy);
                tvteamneutral.setImageResource(R.drawable.ic_neutralselected);
                tvteamsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvteamsad:
                responseteam = 3;
                tvteamhappy.setImageResource(R.drawable.ic_happy);
                tvteamneutral.setImageResource(R.drawable.ic_neutral);
                tvteamsad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.tvcareerhappy:
                responsecareer = 1;
                tvcareerhappy.setImageResource(R.drawable.ic_happyselected);
                tvcareerneutral.setImageResource(R.drawable.ic_neutral);
                tvcareersad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvcareerneutral:
                responsecareer = 2;
                tvcareerhappy.setImageResource(R.drawable.ic_happy);
                tvcareerneutral.setImageResource(R.drawable.ic_neutralselected);
                tvcareersad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvcareersad:
                responsecareer = 3;
                tvcareerhappy.setImageResource(R.drawable.ic_happy);
                tvcareerneutral.setImageResource(R.drawable.ic_neutral);
                tvcareersad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.tvmanagementhappy:
                responsemanagement = 1;
                tvmanagementhappy.setImageResource(R.drawable.ic_happyselected);
                tvmanagementneutral.setImageResource(R.drawable.ic_neutral);
                tvmanagementsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvmanagementneutral:
                responsemanagement = 2;
                tvmanagementhappy.setImageResource(R.drawable.ic_happy);
                tvmanagementneutral.setImageResource(R.drawable.ic_neutralselected);
                tvmanagementsad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvmanagementsad:
                responsemanagement = 3;
                tvmanagementhappy.setImageResource(R.drawable.ic_happy);
                tvmanagementneutral.setImageResource(R.drawable.ic_neutral);
                tvmanagementsad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.tvsuccesshappy:
                responsesuccess = 1;
                tvsuccesshappy.setImageResource(R.drawable.ic_happyselected);
                tvsuccessneutral.setImageResource(R.drawable.ic_neutral);
                tvsuccesssad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvsuccessneutral:
                responsesuccess = 2;
                tvsuccesshappy.setImageResource(R.drawable.ic_happy);
                tvsuccessneutral.setImageResource(R.drawable.ic_neutralselected);
                tvsuccesssad.setImageResource(R.drawable.ic_sad);
                break;
            case R.id.tvsuccesssad:
                responsesuccess = 3;
                tvsuccesshappy.setImageResource(R.drawable.ic_happy);
                tvsuccesssad.setImageResource(R.drawable.ic_sadselected);
                tvsuccessneutral.setImageResource(R.drawable.ic_neutral);
                break;

            case R.id.button4:
                if ((responsebenefits == 1 || responsebenefits == 2 || responsebenefits == 3) && (responsecommunication == 1 || responsecommunication == 2 || responsecommunication == 3) && (responsemanagement == 1 || responsemanagement == 2 || responsemanagement == 3) && (responseteam == 1 || responseteam == 2 || responseteam == 3) && (responsecareer == 1 || responsecareer == 2 || responsecareer == 3) && (responsesuccess == 1 || responsesuccess == 2 || responsesuccess == 3)) {
                    if (com.actiknow.plothr.utils.NetworkConnection.isNetworkAvailable(SurveyActivity.this)) {
                        if(employeePref.getStringPref(SurveyActivity.this, EmployeePref.BY_PASS_OTP).equalsIgnoreCase("911911")){
                            moveTaskToBack(true);
                            finish();
                        }else {
                            //     new submitsurvey().execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main), (String.valueOf(responsebenefits)), (String.valueOf(responsecommunication)), (String.valueOf(responsemanagement)), (String.valueOf(responseteam)), (String.valueOf(responsecareer)), (String.valueOf(responsesuccess)), (comment));
                            SubmitSurvey((String.valueOf(employeePref.getIntPref(this,EmployeePref.EMPLOYEE_ID))), (String.valueOf(responsebenefits)), (String.valueOf(responsecommunication)), (String.valueOf(responsemanagement)), (String.valueOf(responseteam)), (String.valueOf(responsecareer)), (String.valueOf(responsesuccess)), (comment));
                        }
                    }
                }
            else
            {
                Toast.makeText(this, "Please select a response first", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }



    private void SubmitSurvey (final String employee_id, final String responsebenefits, final String responsecommunication, final String responsemanagement, final String responseteam, final String responsecareer, final String responsesuccess, final String comment) {
        if (NetworkConnection.isNetworkAvailable (SurveyActivity.this)) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMITSURVEY, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMITSURVEY,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    int status = jsonObj.getInt("status");
                                    progressDialogVolley.dismiss ();
                                    Utils.showToast (SurveyActivity.this, jsonObj.getString("message"), true);
                                    finish();;
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showToast (SurveyActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showToast (SurveyActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialogVolley.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialogVolley.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showToast (SurveyActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.EMPLOYEE_ID,employee_id);
                    params.put (AppConfigTags.RESPONSEBENEFITS,responsebenefits);
                    params.put (AppConfigTags.RESPONSECOMMUNICATION,responsecommunication);
                    params.put (AppConfigTags.RESPONSEMANAGEMENT,responsemanagement);
                    params.put (AppConfigTags.RESPONSETEAM,responseteam);
                    params.put (AppConfigTags.RESPONSECAREER,responsecareer);
                    params.put (AppConfigTags.RESPONSESUCCESS,responsesuccess);
                    params.put (AppConfigTags.COMMENT,comment);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
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
            Utils.sendRequest (strRequest1, 60);
        } else {

        }
        moveTaskToBack(true);
        finish();

    }



  /* private class submitsurvey extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SurveyActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... arg) {
            String employee_id = arg[0];
            String responsebenefits = arg[1];
            String responsecommunication = arg[2];
            String responsemanagement = arg[3];
            String responseteam = arg[4];
            String responsecareer = arg[5];
            String responsesuccess = arg[6];
            comment = arg[7];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("employee_id", employee_id));
            Log.d("employee_id", employee_id);
            params.add(new BasicNameValuePair("responsebenefits", responsebenefits));
            Log.d("responsebenefits", responsebenefits);
            params.add(new BasicNameValuePair("responsecommunication", responsecommunication));
            Log.d("responsecommunication", responsecommunication);
            params.add(new BasicNameValuePair("responsemanagement", responsemanagement));
            Log.d("responsemanagement", responsemanagement);
            params.add(new BasicNameValuePair("responseteam", responseteam));
            Log.d("responseteam", responseteam);
            params.add(new BasicNameValuePair("responsecareer", responsecareer));
            Log.d("responsecareer", responsecareer);
            params.add(new BasicNameValuePair("responsesuccess", responsesuccess));
            Log.d("responsesuccess", responsesuccess);
            params.add(new BasicNameValuePair("comment", comment));
            Log.d("responsesuccess", comment);
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + com.actiknow.plothr.utils.AppConfigURL.URL_SUBMITSURVEY);
            String json = serviceClient.makeServiceCall(com.actiknow.plothr.utils.AppConfigURL.URL_SUBMITSURVEY, ServiceHandler.POST, params);
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
                }
                catch (JSONException e) {
                    Log.d("catch", "in the catch");
                    e.printStackTrace();
                }
            }
            else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            finish();
        }
    }*/
}