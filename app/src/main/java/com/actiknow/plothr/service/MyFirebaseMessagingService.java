package com.actiknow.plothr.service;

import android.util.Log;

import com.actiknow.plothr.model.NotificationTest;
import com.actiknow.plothr.utils.NotificationUtils;
import com.actiknow.plothr.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("karman", "message received");
        try {
            JSONObject mainObject = new JSONObject(remoteMessage.getData ().toString ());
            JSONObject uniObject = mainObject.getJSONObject("data");
            Log.e("message",uniObject.getString("message"));
        } catch (JSONException e) {
            Utils.showLog (Log.ERROR, "sud", "JSON Exception: " + e.getMessage (), true);
        } catch (Exception e) {
            Utils.showLog (Log.ERROR, "sud", "Exception: " + e.getMessage (), true);
        }
//        Utils.showLog (Log.DEBUG, TAG, "from " + remoteMessage.getFrom (), true);
//        Utils.showLog (Log.DEBUG, TAG, "To " + remoteMessage.getTo (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Collapse Key " + remoteMessage.getCollapseKey (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Message ID " + remoteMessage.getMessageId (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Message Type" + remoteMessage.getMessageType (), true);
//        Utils.showLog (Log.DEBUG, TAG, "Sent Time " + remoteMessage.getSentTime (), true);
//        Utils.showLog (Log.DEBUG, TAG, "TTL " + remoteMessage.getTtl (), true);
    
        if (remoteMessage == null)
            return;
            if (remoteMessage.getData ().size () > 0) {
                Utils.showLog (Log.DEBUG, TAG, "Data Payload: " + remoteMessage.getData ().toString (), true);
                try {
                    NotificationTest notification = new NotificationTest();

                    try {
                        JSONObject mainObject = new JSONObject(remoteMessage.getData ().toString ());
                        JSONObject data = mainObject.getJSONObject("data");
                        Log.e("messagedata",data.getString("message"));
                     //   JSONObject data = remoteMessage.getData ().toString ().getJSONObject ("data");
                        notification.setBackground (data.getBoolean ("is_background"));
                        notification.setTitle (data.getString ("title"));
                        notification.setMessage (data.getString ("message"));
                        //       Log.e("Message",new String(data.getString ("message").getBytes ("ISO-8859-1"), "UTF-8"));
                        notification.setImage_url (data.getString ("image"));
                        notification.setTimestamp (data.getString ("timestamp"));
                        notification.setPayload (data.getJSONObject ("payload"));

                        JSONObject payload = data.getJSONObject ("payload");
                        notification.setNotification_style (payload.getInt ("notification_style"));
                        notification.setNotification_type (payload.getInt ("notification_type"));
                        notification.setNotification_priority (payload.getInt ("notification_priority"));
                    } catch (JSONException e) {
                        Utils.showLog (Log.ERROR, TAG, "JSON Exception: " + e.getMessage (), true);
                    } catch (Exception e) {
                        Utils.showLog (Log.ERROR, TAG, "Exception: " + e.getMessage (), true);
                    }
                    new NotificationUtils(getApplicationContext ()).showNotificationMessage (notification);
                    //handleDataMessage (new JSONObject (remoteMessage.getData ().toString ()));
                   // new NotificationUtils(getApplicationContext ()).showNotificationMessage (new JSONObject (remoteMessage.getData ().toString ()));
                } catch (Exception e) {
                    Utils.showLog (Log.ERROR, TAG, "Exception: " + e.getMessage (), true);
                }
            }
    }


    
    private void handleDataMessage (JSONObject notificationData) {
/*        NotificationTest notification = new NotificationTest();

        try {
            JSONObject data = notificationData.getJSONObject ("data");
            notification.setBackground (data.getBoolean ("is_background"));
            notification.setTitle (new String(data.getString ("title").getBytes ("ISO-8859-1"), "UTF-8"));
            notification.setMessage (new String(data.getString ("message").getBytes ("ISO-8859-1"), "UTF-8"));
     //       Log.e("Message",new String(data.getString ("message").getBytes ("ISO-8859-1"), "UTF-8"));
            notification.setImage_url (data.getString ("image"));
            notification.setTimestamp (data.getString ("timestamp"));
            notification.setPayload (data.getJSONObject ("payload"));

            JSONObject payload = data.getJSONObject ("payload");
            notification.setNotification_style (payload.getInt ("notification_style"));
            notification.setNotification_type (payload.getInt ("notification_type"));
            notification.setNotification_priority (payload.getInt ("notification_priority"));
        } catch (JSONException e) {
            Utils.showLog (Log.ERROR, TAG, "JSON Exception: " + e.getMessage (), true);
        } catch (Exception e) {
            Utils.showLog (Log.ERROR, TAG, "Exception: " + e.getMessage (), true);
        }
        new NotificationUtils(getApplicationContext ()).showNotificationMessage (notification);*/
    }
}
