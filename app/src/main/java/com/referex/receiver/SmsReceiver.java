package com.referex.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


import com.referex.utils.Constants;
import com.referex.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    int message_type = 0; // 0=> default, 1=> OTP message
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;

    public static void bindListener (SmsListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive (Context context, Intent intent) {
        try {
            if (intent.getAction ().equals ("android.provider.Telephony.SMS_RECEIVED")) {
                bundle = intent.getExtras ();
                if (bundle != null) {
                    Object[] pdu_Objects = (Object[]) bundle.get ("pdus");
                    if (pdu_Objects != null) {
                        for (Object aObject : pdu_Objects) {
                            currentSMS = getIncomingMessage (aObject, bundle);
                            String sender = currentSMS.getDisplayOriginatingAddress ();
                            if (sender.contains (Constants.sms_provider)) {
                                message = currentSMS.getDisplayMessageBody ();
                                if (message.toUpperCase ().contains ("OTP")) {
                                    Pattern p = Pattern.compile ("(|^)\\d{6}");
                                    message_type = 1;
                                    Matcher m = p.matcher (message);
                                    if (m.find ()) {
                                        mListener.messageReceived (message, message_type);
                                    } else {
                                        //something went wrong
                                    }
                                }
                            }
                        }
                        this.abortBroadcast ();
                        // End of loop
                    }
                }
            } // bundle null
        } catch (Exception e) {
            e.printStackTrace ();
            Utils.showLog (Log.WARN, "SMS Exception", "Exception in sms parsing", true);
        }
    }

    private SmsMessage getIncomingMessage (Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString ("format");
            currentSMS = SmsMessage.createFromPdu ((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu ((byte[]) aObject);
        }
        return currentSMS;
    }

}
