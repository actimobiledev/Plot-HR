package com.actiknow.plothr.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.actiknow.plothr.R;
import com.actiknow.plothr.activity.MainActivity;
import com.actiknow.plothr.activity.SurveyActivity;
import com.actiknow.plothr.asyn.SubmitResponse;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.actiknow.plothr.utils.Constants.response;


public class NotificationUtils {
// Notification Type => 1=> new property add, 2=>, 3=>
// Notification Style => 1=> simple_notification, 2=>inbox_style, 3=>big_text_style, 4=>big_picture_style, 5=> custom layout
// Notification Priority => -2=>PRIORITY_MIN, -1=>PRIORITY_LOW, 0=>PRIORITY_DEFAULT, 1=>PRIORITY_HIGH, 2=>PRIORITY_MAX

    private static String TAG = NotificationUtils.class.getSimpleName();
    Context mContext;
    SharedPreferences prfs;
    ProgressDialog progressDialogVolley;



    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
       // progressDialogVolley = new ProgressDialog(mContext);
        prfs = mContext.getSharedPreferences("EMPLOYEE_DETAIL", Context.MODE_PRIVATE);
        Constants.employeeID = prfs.getString("employee_id", "");
        Constants.employee_id_main = prfs.getInt("employee_id_main", 0);

    }

    public void showNotificationMessage(com.actiknow.plothr.model.NotificationTest notification) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        if (TextUtils.isEmpty(notification.getMessage()))
            return;

        PendingIntent pendingIntent = null;
        Notification notification1;
        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        switch (notification.getNotification_type()) {
            case 1:

                RemoteViews new_property_expanded = new RemoteViews(mContext.getPackageName(), R.layout.status_bar_expanded);
                RemoteViews new_property_small = new RemoteViews(mContext.getPackageName(), R.layout.status_bar);

                //    try {
                //        JSONObject jsonObject = notification.getPayload ();

                //    Intent notificationIntent = new Intent (mContext, PropertyDetailActivity.class);
                //    notificationIntent.putExtra (AppConfigTags.PROPERTY_ID, jsonObject.getInt (AppConfigTags.PROPERTY_ID));
                //    notificationIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //    pendingIntent = PendingIntent.getActivity (mContext, 0, notificationIntent, 0);

                //   new_property_small.setImageViewBitmap (R.id.ivNotificationTitle, Utils.textAsBitmap (mContext, notification.getTitle (), 18, Color.WHITE));

                //   new_property_expanded.setImageViewBitmap (R.id.ivNotificationTitle, Utils.textAsBitmap (mContext, notification.getTitle (), 18, Color.WHITE));
                //   new_property_expanded.setImageViewBitmap (R.id.ivAddress1, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_ADDRESS), 10, Color.WHITE));
                //   new_property_expanded.setImageViewBitmap (R.id.ivAddress2, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_ADDRESS2), 10, Color.WHITE));
                //   new_property_expanded.setImageViewBitmap (R.id.iv1, Utils.textAsBitmap (mContext, "Beds", 10, Color.WHITE));
                //   new_property_expanded.setImageViewBitmap (R.id.ivBedroom, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BEDROOMS), 16, Color.WHITE));
                //   new_property_expanded.setImageViewBitmap (R.id.iv2, Utils.textAsBitmap (mContext, "Baths", 10, Color.WHITE));
                //  new_property_expanded.setImageViewBitmap (R.id.ivBathroom, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BATHROOMS), 16, Color.WHITE));
                //  new_property_expanded.setImageViewBitmap (R.id.iv3, Utils.textAsBitmap (mContext, "SF", 10, Color.WHITE));
                //  new_property_expanded.setImageViewBitmap (R.id.ivSqFeet, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_AREA), 16, Color.WHITE));
                //  new_property_expanded.setImageViewBitmap (R.id.ivBuildYear, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_BUILT_YEAR), 16, Color.WHITE));
                //  new_property_expanded.setImageViewBitmap (R.id.ivPropertyPrice, Utils.textAsBitmap (mContext, jsonObject.getString (AppConfigTags.PROPERTY_PRICE), 16, Color.WHITE));

                new_property_expanded.setTextViewText(R.id.textView, notification.getTitle());
                //      } catch (JSONException e) {
                //         e.printStackTrace ();
                //     }

                mBuilder.setCustomBigContentView(new_property_expanded)
                        .setCustomContentView(new_property_small)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setStyle(new NotificationCompat.BigPictureStyle());

                notification1 = mBuilder.build();
                notification1.contentIntent = pendingIntent;

                notification1.flags |= Notification.FLAG_AUTO_CANCEL; //Do not clear  the notification
                notification1.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification1.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
                notification1.defaults |= Notification.DEFAULT_SOUND; // Sound


                Intent notificationIntentNeutral = new Intent(mContext, ResponseListenerNeutral.class);
                notificationIntentNeutral.putExtra("response", 2);
                notificationIntentNeutral.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntentNeutral = PendingIntent.getBroadcast(mContext, 0,notificationIntentNeutral, 0);
                new_property_expanded.setOnClickPendingIntent(R.id.ibNotificationNeutral,pendingIntentNeutral);


                Intent notificationIntenthappy = new Intent(mContext, ResponseListenerHappy.class);
                notificationIntenthappy.putExtra("response", 1);
                notificationIntenthappy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntenthappy = PendingIntent.getBroadcast(mContext, 0,notificationIntenthappy, 0);
                new_property_expanded.setOnClickPendingIntent(R.id.ibNotificationHappy,pendingIntenthappy);


                Intent notificationIntentSad = new Intent(mContext, ResponseListenerSad.class);
                notificationIntentSad.putExtra("response", 3);
                notificationIntentSad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntentSad = PendingIntent.getBroadcast(mContext, 0,notificationIntentSad, 0);
                new_property_expanded.setOnClickPendingIntent(R.id.ibNotificationSad,pendingIntentSad);



                Intent intentCancel = new Intent(mContext, ResponseListenerCancel.class);
                intentCancel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pIntentCancel = PendingIntent.getBroadcast(mContext, 0, intentCancel, 0);
                new_property_expanded.setOnClickPendingIntent (R.id.ibNotificationCancel, pIntentCancel);

                mNotifyManager.notify(notification.getNotification_type(), notification1);
                break;

           /* case 2:
                Intent intent = new Intent(mContext, AboutUsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(mContext, 1410,
                        intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder notificationBuilder = new
                        NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.hometrust_notification_icon)
                        .setContentTitle(notification.getTitle ())
                        .setContentText(notification.getMessage())
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent1);

                NotificationManager notificationManager =
                        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1410, notificationBuilder.build());*/

        }

    }

    public static class ResponseListenerHappy extends BroadcastReceiver {
        public ResponseListenerHappy() {
        }
        @Override
        public void onReceive(Context mContext, Intent intent) {
            Log.d("Here", "Happy");
            int response = intent.getIntExtra("response",0);
            SubmitResponseNotification(String.valueOf(response),String.valueOf(Constants.employee_id_main), mContext);
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
    }

    public static class ResponseListenerNeutral extends BroadcastReceiver {
        public ResponseListenerNeutral() {
        }
        @Override
        public void onReceive(Context mContext, Intent intent) {
            Log.d("Here", "Neutral");
            int response = intent.getIntExtra("response",0);
            SubmitResponseNotification(String.valueOf(response),String.valueOf(Constants.employee_id_main), mContext);
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
    }

    public static class ResponseListenerSad extends BroadcastReceiver {
        public ResponseListenerSad() {
        }
        @Override
        public void onReceive(Context mContext, Intent intent) {
            Log.d("Here", "SAd");
            int response = intent.getIntExtra("response",0);
            SubmitResponseNotification(String.valueOf(response),String.valueOf(Constants.employee_id_main), mContext);
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
    }

    public static class ResponseListenerCancel extends BroadcastReceiver {
        public ResponseListenerCancel() {
        }
        @Override
        public void onReceive(Context mContext, Intent intent) {
            Log.d("Here", "Happy");
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(1);
        }
    }


    public static void SubmitResponseNotification (final String responsevalue, final String employee_id, final Context mContext) {

        if (NetworkConnection.isNetworkAvailable (mContext)) {

           // Utils.showProgressDialog (progressDialogVolley, "Please Wait", true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMITRESPONSE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMITRESPONSE,
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
                                    if ((Constants.status == 1) && responsevalue.equalsIgnoreCase("3")) {
                                        Intent intent1 = new Intent(mContext, SurveyActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent1.putExtra("employee_id_main", Constants.employee_id_main);
                                        mContext.startActivity(intent1);
                                        ((Activity) mContext).overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else if ((Constants.status == 2) && (responsevalue.equalsIgnoreCase("1")) || responsevalue.equalsIgnoreCase("2") || responsevalue.equalsIgnoreCase("3")) {
                                        Toast.makeText(mContext, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
                                    } else if ((Constants.status == 1) && responsevalue.equalsIgnoreCase("1") || responsevalue.equalsIgnoreCase("2")) {
                                        Toast.makeText(mContext, "Thanks for Submitting Your response", Toast.LENGTH_SHORT).show();
                                    } else if ((Constants.status == 0) && responsevalue.equalsIgnoreCase("1") || responsevalue.equalsIgnoreCase("2") || responsevalue.equalsIgnoreCase("3")) {
                                        Toast.makeText(mContext, "Sorry you have already submitted your response", Toast.LENGTH_SHORT).show();
                                    }
                                    //boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    //String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    //  if (! error) {
                                    // Utils.showToast (MainActivity.this(), message, true);



                                    // } else {
                                    //  Utils.showToast (MainActivity.this, message, true);
                                    // }
                                 //   progressDialogVolley.dismiss ();
                                } catch (Exception e) {
                                   // progressDialogVolley.dismiss ();
                                   // Utils.showToast (mContext, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                    e.printStackTrace ();
                                }
                            } else {
                               // Utils.showToast (mContext, getResources ().getString (R.string.snackbar_text_error_occurred), true);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                           // progressDialogVolley.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                           // progressDialogVolley.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            //Utils.showToast (mContext, getResources ().getString (R.string.snackbar_text_error_occurred), true);
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
                    Map<String, String> params = new HashMap<>();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }*/
            };
            Utils.sendRequest (strRequest1, 60);
        } else {

        }

    }

}