package com.actiknow.plothr.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;



/**
 * Created by SUDHANSHU SHARMA on 25-02-2016.
 */
public class LoginMobileFragment extends Fragment {

    ProgressDialog progressDialogVolley;
    CoordinatorLayout clMain;
    EditText etMobile;
    Button btlogin;
    int saved_mobile_status = 0;  // 0 => default (nothing entered), 1 => incomplete, 2 => incorrect, 3 => correct
    EmployeePref employeePref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mobile_login, null);
        initView(v);
        initData();
        initListener();

        return v;
    }

    private void initListener() {
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                if(Constants.mobile_number.equals("")){
                    etMobile.setError("Empty Mobile Number");

                }
                else {
                    if (NetworkConnection.isNetworkAvailable(getActivity())) {
                        //new GetOTP().execute(Constants.mobile_number);
                            Log.e("Dynamic","Dynamic");
                            GetOTP(String.valueOf(Constants.mobile_number));
                        }
                    }

            }
        });
    }

    private void initData() {
        employeePref = EmployeePref.getInstance();
        progressDialogVolley = new ProgressDialog(getActivity());
        switch (saved_mobile_status) {
            case 1:
                etMobile.setError("Enter a complete Mobile number");
                break;
            case 2:
                etMobile.setError("Enter a valid Mobile number");
                break;
            case 4:
                etMobile.setError("Enter a Mobile number");
                break;
        }
        etMobile.setMaxEms(10);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                Constants.mobile_number = mEdit.toString();
                int valid;
                valid = isValidMobile(Constants.mobile_number);
                switch (valid) {
                    case 1:
                        etMobile.setError("Enter a complete Mobile number");
                        break;
                    case 2:
                        etMobile.setError("Enter a valid Mobile number");
                        break;
                    case 3:
         /*               View view2 = getActivity ().getCurrentFocus ();
                        if (view2 != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity ().getSystemService (Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow (view2.getWindowToken (), 0);
                        }
             */
                        break;
                    case 4:
                        etMobile.setError("Enter a Mobile number");
                        break;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    private void initView(View v) {
        etMobile = (EditText) v.findViewById(R.id.editTextmobilenumber);
        btlogin = (Button) v.findViewById(R.id.btnext);
        clMain = (CoordinatorLayout) v.findViewById(R.id.clMain);
    }

    private int isValidMobile(String phone2) {
        if (Constants.mobile_number.length() == 0)
            Constants.first_char = "0";
        else
            Constants.first_char = Constants.mobile_number.substring(0, 1);
        if (phone2.length() == 10 && Integer.parseInt(Constants.first_char) > 6) {
            Constants.number_status = 3;
        } else if (phone2.length() < 10 && Integer.parseInt(Constants.first_char) > 6) {
            Constants.number_status = 1;
        } else if (Integer.parseInt(Constants.first_char) <= 6 && Integer.parseInt(Constants.first_char) > 0 && phone2.length() <= 10 && phone2.length() > 1) {
            Constants.number_status = 2;
        } else if (Constants.first_char == "0") {
            Constants.number_status = 4;
        }
        return Constants.number_status;
    }


    private void GetOTP (final String mobile_number) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GETOTP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_GETOTP,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    Constants.otp = jsonObj.getInt(AppConfigTags.OTP);
                                        FragmentManager fragmentManager = getFragmentManager ();
                                        FragmentTransaction fragmentTransaction;
                                        fragmentTransaction = fragmentManager.beginTransaction ();
                                        LoginOtpFragment f3 = new LoginOtpFragment();
                                        Bundle args = new Bundle();
                                        args.putString(AppConfigTags.MOBILE, Constants.mobile_number);
                                        f3.setArguments(args);
                                        // fragmentTransaction.setCustomAnimations (R.anim.fade_in, R.anim.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, f3, "fragment3");
                                        fragmentTransaction.commit();
                                   // Toast.makeText(getActivity(),"OTP : "+Constants.otp, Toast.LENGTH_SHORT).show();

                                    employeePref.putStringPref(getActivity(),EmployeePref.MOBILE,Constants.mobile_number);
                                    employeePref.putIntPref(getActivity(),EmployeePref.OTP,jsonObj.getInt(AppConfigTags.OTP));
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

  /*  private class GetOTP extends AsyncTask<String, Void, Void> {
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
            Log.d("url: ", "> " + AppConfigURL.URL_GETOTP);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_GETOTP, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArrayOTP = jsonObj.getJSONArray("details");
                    Log.d("json array", "" + jsonArrayOTP);
                    int len = jsonArrayOTP.length();
                    Log.d("len", "get array length");
                    for (int i = 0; i < jsonArrayOTP.length(); i++) {
                        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                        JSONObject c = jsonArrayOTP.getJSONObject(i);
                        Constants.otp = c.getInt("otp");
                        map2.put("otp", Constants.otp);
                        Log.d("otp", "" + Constants.otp);
                        arrayListOTP.add(map2);
                       // Toast.makeText(LoginMobileFragment.this, "" +  Constants.otp, Toast.LENGTH_LONG).show();
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
            FragmentManager fragmentManager = getFragmentManager ();
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction ();
            com.actiknow.plothr.Fragment.LoginOtpFragment f3 = new com.actiknow.plothr.Fragment.LoginOtpFragment();
            Bundle args = new Bundle();
            args.putString("mobile", Constants.mobile_number);
            f3.setArguments(args);
            // fragmentTransaction.setCustomAnimations (R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.fragment_container, f3, "fragment2");
            fragmentTransaction.commit();
            Toast.makeText(getActivity(), "otp:" + Constants.otp, Toast.LENGTH_LONG).show();
            for (HashMap<String, Integer> map : arrayListOTP) {
                for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    int value = mapEntry.getValue();
                    if (key == "otp") {
                        Constants.otp = value;
                    }
                }
            }

        }


    }*/
}