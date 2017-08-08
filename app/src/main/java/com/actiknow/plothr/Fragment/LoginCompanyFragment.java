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
 * Created by SUDHANSHU SHARMA on 24-02-2016.
 */

public class LoginCompanyFragment extends Fragment {
    ProgressDialog progressDialogVolley;
    CoordinatorLayout clMain;
    EditText etCompanyID;
    Button btsubmit;
    String saved_companyID = "";
    EmployeePref employeePref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_company_login, null);
        initView(v);
        initData();
        initListener();
        return v;
    }

    private void initListener() {
        etCompanyID.addTextChangedListener (new TextWatcher() {
            @Override
            public void afterTextChanged (Editable mEdit) {
                Constants.companyCode = mEdit.toString ();
            }
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }
        });
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                if (Constants.companyCode.length() == 0) {
                    etCompanyID.setError("Enter Company ID");
                }
                if (Constants.companyCode.length() != 0) {
                    if (NetworkConnection.isNetworkAvailable(getActivity())){

                            //new checkLogin().execute(Constants.companyCode);
                            CheckCompanyLogin(Constants.companyCode);


                    }


                }
            }
        });
    }

    private void initData() {
        progressDialogVolley = new ProgressDialog(getActivity());
        employeePref = EmployeePref.getInstance();
    }

    private void initView(View v) {
        etCompanyID = (EditText) v.findViewById(R.id.editTextcompanyid);
        btsubmit = (Button) v.findViewById(R.id.button);
        clMain = (CoordinatorLayout) v.findViewById(R.id.clMain);
        saved_companyID = getArguments().getString("companyid");
    }


    private void CheckCompanyLogin (final String company_code) {
        if (NetworkConnection.isNetworkAvailable (getActivity())) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CHECKCOMPANY, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_CHECKCOMPANY,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    int status = jsonObj.getInt(AppConfigTags.STATUS);
                                    Constants.companyID = jsonObj.getString(AppConfigTags.COMPANY_ID);
                                    if(status == 1){
                                        employeePref.putStringPref(getActivity(),EmployeePref.COMPANY_ID,jsonObj.getString(AppConfigTags.COMPANY_ID));
                                        FragmentManager fragmentManager = getFragmentManager ();
                                        FragmentTransaction fragmentTransaction;
                                        fragmentTransaction = fragmentManager.beginTransaction ();
                                        LoginMobileFragment f2 = new LoginMobileFragment();
                                        fragmentTransaction.replace(R.id.fragment_container, f2, "fragment2");
                                        fragmentTransaction.commit();
                                       // Constants.companyID = String.valueOf(value);
                                    }else{
                                        Utils.showSnackBar (getActivity(), clMain, message, Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                       // getActivity().finish();
                                    }

                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity(), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                               // Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
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
                    params.put (AppConfigTags.COMPANY_CODE,company_code);
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

    /*private class checkLogin extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String company_code = arg[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("company_code", company_code));
            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + AppConfigURL.URL_CHECKLOGIN);
            String json = serviceClient.makeServiceCall(AppConfigURL.URL_CHECKLOGIN, ServiceHandler.POST, params);
            Log.d("Get match fixture response: ", "> " + json);
            if (json != null) {
                try {
                    Log.d("try", "in the try");
                    JSONObject jsonObj = new JSONObject(json);
                    Log.d("jsonObject", "new json Object");
                    jsonArrayCheckLogin = jsonObj.getJSONArray("details");
                    Log.d("json array", "" + jsonArrayCheckLogin);
                    int len = jsonArrayCheckLogin.length();
                    Log.d("len", "get array length");
                    for (int i = 0; i < jsonArrayCheckLogin.length(); i++) {
                        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                        JSONObject c = jsonArrayCheckLogin.getJSONObject(i);
                        status = c.getInt("status");
                        Constants.companyID = c.getString("company_id");
                        map2.put("status", status);
                        Log.d("status", "" + status);
                        map2.put("company_id", Integer.parseInt(Constants.companyID));
                        Log.d("company_id", "" + Constants.companyID);
                        if (status == 0) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Company code entered is not valid", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        arrayListCheckLogin.add(map2);
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
            progressDialog.dismiss();
            for (HashMap<String, Integer> map : arrayListCheckLogin) {
                for (Map.Entry<String, Integer> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    int value = mapEntry.getValue();
                    if (key == "status") {
                        status = value;FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = fragmentManager.beginTransaction();
                        com.actiknow.plothr.Fragment.LoginMobileFragment f2 = new com.actiknow.plothr.Fragment.LoginMobileFragment();
                        fragmentTransaction.replace(R.id.fragment_container, f2, "fragment2");
                        fragmentTransaction.commit();
                    } else if (key == "company_id") {
                        Constants.companyID = String.valueOf(value);
                    }
                }
            }
            if (status == 1) {

            }
//           }
        }
    }*/

}
