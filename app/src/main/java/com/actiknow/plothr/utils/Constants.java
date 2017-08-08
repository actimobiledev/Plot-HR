package com.actiknow.plothr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.actiknow.plothr.R;


public class Constants {
    public static int response = 0;    //  0 => default
    public static int employee_id_main = 0;    //  0 => default
    public static String companyCode = "";
    public static String companyID = "";
    public static String employeeID = "";
    public static String mobile_number = "";
    public static int status = 0;
    public static boolean show_log = true;

    public static int number_status = 0 ; // 0 => default (nothing entered), 1 => incomplete, 2 => incorrect, 3 => correct, 4 => no mobile number entered
    public static int otp=0;
    public static String otp_entered ="";
    public static int by_pass_otp =0;
    public static int match=0;   // 0 => no match found, 1 => match found
    public static String first_char ="";

    public static String regid = "";



    public interface ACTION   {
        public static String HAPPY = "happy";
        public static String NEUTRAL = "neutral";
        public static String SAD = "sad";
        public static String CANCEL_NOTIFICATION = "cancel_notification";
        public static String START_NOTIFICATION = "start_notification";

        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt (Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options ();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_album_art, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}