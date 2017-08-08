package com.actiknow.plothr.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.actiknow.plothr.R;
import com.actiknow.plothr.activity.MainActivity;


public class NotificationService extends Service {

    @Override
    public void onDestroy () {
        super.onDestroy ();
    }

    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        SharedPreferences prfs = getSharedPreferences("EMPLOYEE_DETAIL", Context.MODE_PRIVATE);
        com.actiknow.plothr.utils.Constants.companyCode = prfs.getString("company_id", "");
        com.actiknow.plothr.utils.Constants.mobile_number = prfs.getString("employee_mobile", "");
        Log.d("mobile_number", "" + com.actiknow.plothr.utils.Constants.mobile_number);
        com.actiknow.plothr.utils.Constants.employeeID = prfs.getString("employee_id", "");
        com.actiknow.plothr.utils.Constants.employee_id_main = prfs.getInt("employee_id_main", 0);
        com.actiknow.plothr.utils.Constants.regid = prfs.getString("gcm_reg_id","");
        Log.d("regid", "" + com.actiknow.plothr.utils.Constants.regid);

        if (intent.getAction ().equals (com.actiknow.plothr.utils.Constants.ACTION.START_NOTIFICATION)) {
            showNotification ();
        } else if (intent.getAction ().equals (com.actiknow.plothr.utils.Constants.ACTION.HAPPY)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i(LOG_TAG, "Happy");

            new com.actiknow.plothr.asyn.SubmitResponse(this).execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main), ("1"));

            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (com.actiknow.plothr.utils.Constants.ACTION.NEUTRAL)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i(LOG_TAG, "Neutral");

            new com.actiknow.plothr.asyn.SubmitResponse(this).execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main), ("2"));

            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (com.actiknow.plothr.utils.Constants.ACTION.SAD)) {
            Toast.makeText (this, "Thank you for submitting your response", Toast.LENGTH_SHORT).show ();
            Log.i(LOG_TAG, "Sad");
            new com.actiknow.plothr.asyn.SubmitResponse(this).execute(String.valueOf(com.actiknow.plothr.utils.Constants.employee_id_main), ("3"));
            stopForeground (true);
            stopSelf ();
        } else if (intent.getAction ().equals (
                com.actiknow.plothr.utils.Constants.ACTION.CANCEL_NOTIFICATION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
//            Toast.makeText (this, "Service Stopped", Toast.LENGTH_SHORT).show ();
            stopForeground (true);
            stopSelf ();
        }
        return START_STICKY;
    }

    Notification notification;
    private final String LOG_TAG = "NotificationService";

    private void showNotification () {
    // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName (),
                    R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName (),
                R.layout.status_bar_expanded);

        views.setViewVisibility (R.id.status_bar_album_art, View.VISIBLE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction (com.actiknow.plothr.utils.Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent intentHappy = new Intent(this, NotificationService.class);
        intentHappy.setAction (com.actiknow.plothr.utils.Constants.ACTION.HAPPY);
        PendingIntent pIntentHappy = PendingIntent.getService(this, 0, intentHappy, 0);

        Intent intentNeutral = new Intent(this, NotificationService.class);
        intentNeutral.setAction (com.actiknow.plothr.utils.Constants.ACTION.NEUTRAL);
        PendingIntent pIntentNeutral = PendingIntent.getService(this, 0, intentNeutral, 0);

        Intent intentSad = new Intent(this, NotificationService.class);
        intentSad.setAction (com.actiknow.plothr.utils.Constants.ACTION.SAD);
        PendingIntent pIntentSad = PendingIntent.getService(this, 0, intentSad, 0);

        Intent intentCancel = new Intent(this, NotificationService.class);
        intentCancel.setAction (com.actiknow.plothr.utils.Constants.ACTION.CANCEL_NOTIFICATION);
        PendingIntent pIntentCancel = PendingIntent.getService(this, 0, intentCancel, 0);

        bigViews.setOnClickPendingIntent (R.id.ibNotificationHappy, pIntentHappy);
        bigViews.setOnClickPendingIntent (R.id.ibNotificationNeutral, pIntentNeutral);
        bigViews.setOnClickPendingIntent (R.id.ibNotificationSad, pIntentSad);
        bigViews.setOnClickPendingIntent (R.id.ibNotificationCancel, pIntentCancel);

        notification = new Notification.Builder (this).build ();
        notification.contentView = views;
        notification.bigContentView = bigViews;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.mipmap.ic_launcher;
        notification.contentIntent = pendingIntent;
        startForeground (com.actiknow.plothr.utils.Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }
}