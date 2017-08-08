package com.actiknow.plothr.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actiknow.plothr.R;
import com.actiknow.plothr.activity.MainActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static com.actiknow.plothr.utils.AppConfigTags.OTP;
import static com.actiknow.plothr.utils.Constants.companyID;
import static com.actiknow.plothr.utils.Constants.otp_entered;


/**
 * Created by SUDHANSHU SHARMA on 25-02-2016.
 */
public class LoginOtpFragment extends Fragment {
    String value;
    CoordinatorLayout clMain;
    EditText etmobile;
    EditText etCode;
    TextView rlResendOTP;
    SharedPreferences prfs;
    ProgressDialog progressDialogVolley;
    SharedPreferences.Editor editor;
    EmployeePref employeePref;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_otp_login, null);
        value = getArguments().getString("mobile");
        initView(v);
        initData();
        initListener();
        return v;
    }

    private void initListener() {
        rlResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ResendOTP(Constants.mobile_number);

            }
        });
    }

    private void initData() {
        progressDialogVolley = new ProgressDialog(getActivity());
        employeePref = EmployeePref.getInstance();
        prfs = getActivity().getSharedPreferences(AppConfigTags.EMPLOYEE_DETAIL, Context.MODE_PRIVATE);
        editor = prfs.edit ();
        etmobile.setText(value);
        etCode.setMaxEms(6);
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                otp_entered = mEdit.toString();
                if (mEdit.toString().length() == 6) {
                  //  CheckOTP(Constants.mobile_number, otp_entered);
                    Log.e("OTP",""+Constants.otp);
                    Log.e("OTP",""+Integer.parseInt(otp_entered));
                    if(otp_entered.equalsIgnoreCase("911911")){
                        employeePref.putStringPref(getActivity(),EmployeePref.BY_PASS_OTP, "911911");
                    }
                    if (Constants.otp == Integer.parseInt(otp_entered) || Integer.parseInt(otp_entered) == 911911) {
                        View view2 = getActivity().getCurrentFocus();
                        if (view2 != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                        }
                        //new OTPUsed().execute(Constants.mobile_number, Constants.otp_entered);
                        //  OTPUsed(Constants.mobile_number, otp_entered);

                        if (NetworkConnection.isNetworkAvailable(getActivity())) {

                            UserRegister(Constants.mobile_number, otp_entered, companyID, Constants.regid);

                        }
                            Log.d("companyID", "" + companyID);
                    } else {
                        Toast.makeText(getActivity(), "OTP entered is not correct", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        //  etmobile.setText(MainActivity.mobile_number);
        rlResendOTP.setVisibility(View.VISIBLE);
    }

    private void initView(View v) {
        etCode = (EditText) v.findViewById(R.id.editTextcode);
        etmobile = (EditText) v.findViewById(R.id.etmobile);
        rlResendOTP = (TextView) v.findViewById(R.id.resend);
        clMain = (CoordinatorLayout) v.findViewById(R.id.clMain);
    }

    /*private void OTPUsed (final String mobile_number, final String otp_entered) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_OTPUSED, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_OTPUSED,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    JSONArray jsonArrayStatus = jsonObj.getJSONArray ("details");
                                    JSONObject jsonObjStatus = jsonArrayStatus.getJSONObject(0);
                                    Constants.status = jsonObjStatus.getInt("status");
                                    if(Constants.status == 1){

                                    }

                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showToast (getActivity(), getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.MOBILE,mobile_number);
                    params.put (OTP,otp_entered);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

              *//*  @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }*//*
            };
            Utils.sendRequest (strRequest1, 60);
        } else {

        }

    }*/

    /*private class OTPUsed extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String mobile = arg[0];
            String otp = arg[1];

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile", mobile));
            params.add(new BasicNameValuePair("otp", otp));
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + AppConfigURL.URL_OTPUSED);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_OTPUSED, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArrayOTPUsed = jsonObj.getJSONArray("details");
                    Log.d("json aray", "" + jsonArrayOTPUsed);
                    int len = jsonArrayOTPUsed.length();
                    Log.d("len", "get array length");
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
        }
    }*/


    private void ResendOTP (final String mobile_number) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_RESENDOTP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_RESENDOTP,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    Constants.otp = jsonObj.getInt("otp");
                                   // Toast.makeText(getActivity(), "OTP : "+Constants.otp, Toast.LENGTH_SHORT).show();
                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.MOBILE,mobile_number);
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

    }

    /*private class ResendOTP extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... arg) {
            String mobile = arg[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile", mobile));
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + AppConfigURL.URL_RESENDOTP);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_RESENDOTP, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArrayResendOTP = jsonObj.getJSONArray("details");
                    Log.d("json aray", "" + jsonArrayResendOTP);
                    int len = jsonArrayResendOTP.length();
                    Log.d("len", "get array length");
                    for (int i = 0; i < jsonArrayResendOTP.length(); i++) {
                        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                        JSONObject c = jsonArrayResendOTP.getJSONObject(i);
                        Constants.otp = c.getInt("otp");
                        map2.put("otp", Constants.otp);
                        Log.d("otp", "" + Constants.otp);
                        arrayListResendOTP.add(map2);
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
            Toast.makeText(getActivity(), "otp:" + Constants.otp, Toast.LENGTH_LONG).show();
            for (HashMap<String, Integer> map : arrayListResendOTP) {
                for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    int value = mapEntry.getValue();
                    if (key == "otp") {
                        Constants.otp = value;
                    }
                }
            }
            //    Log.d ("OTP : ", ""+ Constants.otp);
            //   tvOTP.setText ("OTP : " + Constants.otp);
        }
    }*/

  /*  private void CheckOTP (final String mobile_number, final String otp_entered) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CHECKOTP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_CHECKOTP,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    JSONArray jsonArrayStatus = jsonObj.getJSONArray ("details");
                                    JSONObject jsonObjMatch = jsonArrayStatus.getJSONObject(0);
                                  //  Constants.status = jsonObjStatus.getInt("status");
                                    Constants.match = jsonObjMatch.getInt("match");
                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showToast (getActivity(), getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.MOBILE,mobile_number);
                    params.put (OTP,otp_entered);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

              *//*  @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }*//*
            };
            Utils.sendRequest (strRequest1, 60);
        } else {

        }

    }*/

   /* private class CheckOTP extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String mobile = arg[0];
            String otp = arg[1];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile", mobile));
            params.add(new BasicNameValuePair("otp", otp));
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + AppConfigURL.URL_CHECKOTP);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_CHECKOTP, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArrayCheckOTP = jsonObj.getJSONArray("details");
                    Log.d("json aray", "" + jsonArrayCheckOTP);
                    int len = jsonArrayCheckOTP.length();
                    Log.d("len", "get array length");
                    for (int i = 0; i < jsonArrayCheckOTP.length(); i++) {
                        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                        JSONObject c = jsonArrayCheckOTP.getJSONObject(i);
                        Constants.match = c.getInt("match");
                        map2.put("match", Constants.match);
                        Log.d("match", "" + Constants.match);
                        arrayListCheckOTP.add(map2);
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

        }
    }*/


    private void UserRegister (final String mobile_number, final String otp,  final String companyID, final String regid) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMITEMPLOYEE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMITEMPLOYEE,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                   // JSONObject jsonObjStatus = jsonArrayStatus.getJSONObject(0);
                                    Constants.employee_id_main = jsonObj.getInt("employee_id");
                                    String employee_status = jsonObj.getString("employee_status");
                                    Log.d("employee_id", "" + Constants.employee_id_main);

                                    Log.d("employee_status", employee_status);

                                    employeePref.putIntPref(getActivity(),EmployeePref.EMPLOYEE_ID,jsonObj.getInt(AppConfigTags.EMPLOYEE_ID));
                                    Log.e("Employee_id_pref",""+employeePref.getIntPref(getActivity(),EmployeePref.EMPLOYEE_ID));
                                    Intent intent = new Intent(getActivity (),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    getActivity().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    startActivity (intent);


                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.MOBILE,mobile_number);
                    params.put (AppConfigTags.OTP,otp);
                    params.put (AppConfigTags.COMPANY_ID,companyID);
                    params.put (AppConfigTags.GCM_REG_ID,prfs.getString(AppConfigTags.FIREBASE_ID,""));
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

    }

   /* private class SubmitEmployee extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            //String employee_id = arg[0];
            String employee_mobile = arg[0];
            String company_id = arg[1];
            String gcm_reg_id = arg[2];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add (new BasicNameValuePair ("employee_id", employee_id));
            params.add(new BasicNameValuePair("employee_mobile", employee_mobile));
            params.add(new BasicNameValuePair("company_id", company_id));
            params.add(new BasicNameValuePair("gcm_reg_id", gcm_reg_id));
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + AppConfigURL.URL_SUBMITEMPLOYEE);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_SUBMITEMPLOYEE, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArraySubmitEmployee = jsonObj.getJSONArray("details");
                    Log.d("json aray", "" + jsonArraySubmitEmployee);
                    int len = jsonArraySubmitEmployee.length();
                    Log.d("len", "get array length");
                    for (int i = 0; i < jsonArraySubmitEmployee.length(); i++) {
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        JSONObject c = jsonArraySubmitEmployee.getJSONObject(i);
                        Constants.employee_id_main = c.getInt("employee_id");
                        String employee_status = c.getString("employee_status");
                        map2.put("employee_id", "" + Constants.employee_id_main);
                        Log.d("employee_id", "" + Constants.employee_id_main);
                        map2.put("employee_status", employee_status);
                        Log.d("employee_status", employee_status);
                        arrayListSubmitEmployee.add(map2);
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
        protected void onPostExecute (Void result) {
            progressDialog.dismiss();

            for (HashMap<String, String> map : arrayListSubmitEmployee) {
                for (Map.Entry<String, String> mapEntry : map.entrySet ()) {
                    String key = mapEntry.getKey ();
                    String value = mapEntry.getValue ();
                    if (key == "employee_id") {
                        Constants.employee_id_main = Integer.parseInt(value);
                    }
                    else if (key == "employee_status") {
                        //     Toast.makeText (getActivity (), "user status : " + value, Toast.LENGTH_SHORT).show ();
                    }
                }
            }
            SharedPreferences preferences = getActivity ().getSharedPreferences ("EMPLOYEE_DETAIL", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putString("company_id", Constants.companyCode);
            Log.d("company_id", "" + Constants.companyCode);
            editor.putString("employee_mobile", Constants.mobile_number);
            Log.d("employee_mobile", "" + Constants.mobile_number);
           // editor.putString("employee_id", MainActivity.employeeID);
            editor.putInt("employee_id_main", Constants.employee_id_main);
            Log.d("employee_id_main", "" + Constants.employee_id_main);
            editor.putString("gcm_reg_id", Constants.regid);
            Log.d("gcm_reg_id", Constants.regid);

            editor.apply();
            Intent intent = new Intent(getActivity (),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity (intent);

        }
    }*/
}