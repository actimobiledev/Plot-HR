package com.actiknow.plothr.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actiknow.plothr.R;
import com.actiknow.plothr.asyn.SubmitResponse;
import com.actiknow.plothr.service.ReminderService;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import static com.actiknow.plothr.utils.Constants.otp_entered;


public class MainActivity extends Activity implements View.OnClickListener {
    ProgressDialog progressDialogVolley;
    CoordinatorLayout clMain;
    Button btSubmit;
    ImageView ivHappy;
    ImageView ivNeutral;
    ImageView ivSad;
    private String TAG = MainActivity.class.getSimpleName ();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedPreferences prfs;
    EmployeePref employeePref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initPreference();
        initListener();
       // startAlarm();

    }

    private void initListener() {
        if (employeePref.getStringPref(this,EmployeePref.COMPANY_ID).equalsIgnoreCase("")) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }else if (employeePref.getStringPref(this,EmployeePref.MOBILE).equalsIgnoreCase("")) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }
        if (employeePref.getStringPref(this,EmployeePref.COMPANY_ID).equalsIgnoreCase("") || employeePref.getStringPref(this,EmployeePref.MOBILE).equalsIgnoreCase(""))
        finish();
        ivHappy.setOnClickListener(this);
        ivNeutral.setOnClickListener(this);
        ivSad.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    private void initData() {
        progressDialogVolley = new ProgressDialog(this);
        employeePref = EmployeePref.getInstance();
    }

    private void initPreference() {
        prfs = getSharedPreferences("EMPLOYEE_DETAIL", Context.MODE_PRIVATE);
        com.actiknow.plothr.utils.Constants.companyCode = prfs.getString("company_id", "");
        com.actiknow.plothr.utils.Constants.mobile_number = prfs.getString("employee_mobile", "");
        com.actiknow.plothr.utils.Constants.employeeID = prfs.getString("employee_id", "");
        com.actiknow.plothr.utils.Constants.employee_id_main = prfs.getInt("employee_id_main", 0);
        Log.e("Buypass Otp",prfs.getString("otp_entered",""));
    }

    private void initView() {
        btSubmit = (Button) findViewById(R.id.buttonsubmit);
        ivHappy = (ImageView) findViewById(R.id.imageViewhappy3);
        ivNeutral = (ImageView) findViewById(R.id.imageViewneutral3);
        ivSad = (ImageView) findViewById(R.id.imageViewsad3);
    }

    // starting the service to register with GCM
  /*  private void registerGCM () {
        Log.d(TAG, "in register gcm");
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra ("key", "register");
        startService (intent);
    }*/

    private boolean checkPlayServices () {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance ();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable (this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError (resultCode)) {
                apiAvailability.getErrorDialog (this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show ();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText (getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show ();
                finish ();
            }
            return false;
        }
        return true;
    }

   /* @Override
    protected void onResume () {
        super.onResume ();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver (mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause () {
        LocalBroadcastManager.getInstance(this).unregisterReceiver (mRegistrationBroadcastReceiver);
        super.onPause ();
    }*/

    @Override
    public void onClick(View v) {
        btSubmit = (Button) findViewById(R.id.buttonsubmit);
        // etRemark = (EditText)findViewById(R.id.editTextremarks);
        ImageView bhappy = (ImageView) findViewById(R.id.imageViewhappy3);
        ImageView bneutral = (ImageView) findViewById(R.id.imageViewneutral3);
        ImageView bsad = (ImageView) findViewById(R.id.imageViewsad3);

        switch (v.getId()) {
            case R.id.imageViewhappy3:
                com.actiknow.plothr.utils.Constants.response = 1;
                bhappy.setImageResource(R.drawable.ic_happyselected);
                bneutral.setImageResource(R.drawable.ic_neutral2);
                bsad.setImageResource(R.drawable.ic_sad2);
                break;
            case R.id.imageViewneutral3:
                com.actiknow.plothr.utils.Constants.response = 2;
                bhappy.setImageResource(R.drawable.ic_happy2);
                bneutral.setImageResource(R.drawable.ic_neutralselected);
                bsad.setImageResource(R.drawable.ic_sad2);
                break;
            case R.id.imageViewsad3:
                com.actiknow.plothr.utils.Constants.response = 3;
                bhappy.setImageResource(R.drawable.ic_happy2);
                bneutral.setImageResource(R.drawable.ic_neutral2);
                bsad.setImageResource(R.drawable.ic_sadselected);
                break;
            case R.id.buttonsubmit:
                if (com.actiknow.plothr.utils.Constants.response == 1 || com.actiknow.plothr.utils.Constants.response == 2 || com.actiknow.plothr.utils.Constants.response == 3) {
                    if (com.actiknow.plothr.utils.NetworkConnection.isNetworkAvailable(MainActivity.this)) {
                        if(employeePref.getStringPref(MainActivity.this,EmployeePref.BY_PASS_OTP).equalsIgnoreCase("911911")){
                            Intent intent1 = new Intent(MainActivity.this, SurveyActivity.class);
                            intent1.putExtra("employee_id_main", Constants.employee_id_main);
                            overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent1);
                        }else {
                            // new SubmitResponse().execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main), (String.valueOf(com.actiknow.plothr.utils.Constants.response)));
                            SubmitResponse(String.valueOf(employeePref.getIntPref(this,EmployeePref.EMPLOYEE_ID)), (String.valueOf(com.actiknow.plothr.utils.Constants.response)));
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

   /* private class SubmitResponse extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String employee_id = arg[0];
            String response = arg[1];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("employee_id", employee_id));
            Log.d("employee_id", employee_id);
            params.add(new BasicNameValuePair("response", response));
            Log.d("response", response);

            ServiceHandler serviceClient = new ServiceHandler();
            Log.d("url: ", "> " + com.actiknow.plothr.utils.AppConfigURL.URL_SUBMITRESPONSE);
            String json = serviceClient.makeServiceCall(com.actiknow.plothr.utils.AppConfigURL.URL_SUBMITRESPONSE, ServiceHandler.POST, params);
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
                        com.actiknow.plothr.utils.Constants.status = c.getInt("status");

                        map2.put("status", com.actiknow.plothr.utils.Constants.status);
                        Log.d("status", "" + com.actiknow.plothr.utils.Constants.status);
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
            progressDialog.dismiss();

            if ((com.actiknow.plothr.utils.Constants.status == 1) && (com.actiknow.plothr.utils.Constants.response == 3)) {
                Intent intent1 = new Intent(MainActivity.this, SurveyActivity.class);
                intent1.putExtra("employee_id_main", com.actiknow.plothr.utils.Constants.employee_id_main);
                startActivity(intent1);
                Log.d("fdvhkjfdvkvefnkvd", "" + com.actiknow.plothr.utils.Constants.response);
            } else if ((com.actiknow.plothr.utils.Constants.status == 2) && (com.actiknow.plothr.utils.Constants.response == 1 || com.actiknow.plothr.utils.Constants.response == 2 || com.actiknow.plothr.utils.Constants.response == 3)) {
                Toast.makeText(MainActivity.this, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
            } else if ((com.actiknow.plothr.utils.Constants.status == 1) && (com.actiknow.plothr.utils.Constants.response == 1 || com.actiknow.plothr.utils.Constants.response == 2)) {
                Toast.makeText(MainActivity.this, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
            } else if ((com.actiknow.plothr.utils.Constants.status == 0) && (com.actiknow.plothr.utils.Constants.response == 1 || com.actiknow.plothr.utils.Constants.response == 2 || com.actiknow.plothr.utils.Constants.response == 3)) {
                Toast.makeText(MainActivity.this, "Sorry you have already submitted your response", Toast.LENGTH_SHORT).show();
            }
            finish();
        }


    }*/

    private void SubmitResponse (final String employee_id, final String responsevalue) {
        if (NetworkConnection.isNetworkAvailable (MainActivity.this)) {
            Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMITRESPONSE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMITRESPONSE,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    int status = jsonObj.getInt("status");
                                    Log.e("STATUS",""+status);
                                    if ((status == 1) && responsevalue.equalsIgnoreCase("3")) {
                                        Intent intent1 = new Intent(MainActivity.this, SurveyActivity.class);
                                        intent1.putExtra("employee_id_main", Constants.employee_id_main);
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                        startActivity(intent1);
                                        Log.d("RESPONSE", "" + Constants.response);
                                    } else if ((status == 2) && (responsevalue.equalsIgnoreCase("1")) || responsevalue.equalsIgnoreCase("2") || responsevalue.equalsIgnoreCase("3")) {
                                        Toast.makeText(MainActivity.this, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if ((status == 1) && responsevalue.equalsIgnoreCase("1") || responsevalue.equalsIgnoreCase("2")) {
                                        Toast.makeText(MainActivity.this, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if ((status == 0) && responsevalue.equalsIgnoreCase("1") || responsevalue.equalsIgnoreCase("2") || responsevalue.equalsIgnoreCase("3")) {
                                        Toast.makeText(MainActivity.this, "Sorry you have already submitted your response", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    //boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    //String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                  //  if (! error) {
                                        // Utils.showToast (MainActivity.this(), message, true);

                                        

                                   // } else {
                                      //  Utils.showToast (MainActivity.this, message, true);
                                   // }
                                    progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                    progressDialogVolley.dismiss ();
                                    Utils.showToast (MainActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showToast (MainActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
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
                            Utils.showToast (MainActivity.this, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.EMPLOYEE_ID,employee_id);
                    params.put (AppConfigTags.RESPONSE,responsevalue);
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

    private void startAlarm() {
        Log.e("called","called");
      /*  AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        long when = System.currentTimeMillis();         // notification time
        Log.e("Current Time",""+when);
        Intent intent = new Intent(this, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, when, (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3), pendingIntent);*/

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(this, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);


        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 17); // At the hour you wanna firetiming = 16:30  == hour = 07, minute= 00, second = 0
        firingCal.set(Calendar.MINUTE, 56); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        Log.e("Current Time",""+currentTime);
        Log.e("Intended Time",""+intendedTime);

        if(intendedTime >= currentTime){
            Log.e("Current time","IS Greater");
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, currentTime, (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3), pendingIntent);
            // alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        } else{
            Log.e("Current time","IS less");
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            // alarmManager.setRepeating(AlarmManager.RTC, currentTime, (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3), pendingIntent);
        }

    }

 }

