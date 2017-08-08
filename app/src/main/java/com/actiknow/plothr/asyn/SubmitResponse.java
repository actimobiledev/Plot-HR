package com.actiknow.plothr.asyn;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.actiknow.plothr.activity.ServiceHandler;
import com.actiknow.plothr.activity.SurveyActivity;
import com.actiknow.plothr.utils.AppConfigURL;
import com.actiknow.plothr.utils.NotificationService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class SubmitResponse extends AsyncTask<String, Void, Void> {

    // int status = 0;
    NotificationService mActivity;
    int status = 0;
    int status2 = 2;

    public SubmitResponse(NotificationService activity) {
        mActivity = activity;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(String... arg) {
        String employee_id = arg[0];
        String response = arg[1];
      //  String remark = arg[2];
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("employee_id", employee_id));
        Log.d("employee_id", employee_id);
        params.add(new BasicNameValuePair("response", response));
        Log.d("response", response);
      //  params.add(new BasicNameValuePair("remark", remark));
     //   Log.d("remark", remark);
        ServiceHandler serviceClient = new ServiceHandler();
        Log.d("url: ", "> " + AppConfigURL.URL_SUBMITRESPONSE);
        String json = serviceClient.makeServiceCall(AppConfigURL.URL_SUBMITRESPONSE, ServiceHandler.POST, params);
        Log.d("Response ", "> " + json);
        if (json != null) {
            try {
                Log.d("try", "in the try");
                JSONObject jsonObj = new JSONObject(json);
                Log.d("jsonObject", "new json Object");
                JSONArray jsonArraySubmitResponse = jsonObj.getJSONArray("details");
                Log.d("json array", "" + jsonArraySubmitResponse);
                for (int i = 0; i < jsonArraySubmitResponse.length(); i++) {
                    HashMap<String, Integer> map2 = new HashMap<String, Integer>();
                    JSONObject c = jsonArraySubmitResponse.getJSONObject(i);
                    status = c.getInt("status");
                    map2.put("status", status);
                    Log.d("status", "" + status);

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
        super.onPostExecute(result);
        if (status == 1) {
            Intent dialogIntent = new Intent(mActivity, SurveyActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(dialogIntent);
            //  Log.d("fdvhkjfdvkvefnkvd", "" + response);
        }

    }
}