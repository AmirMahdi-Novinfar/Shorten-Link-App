package ir.iamnovinfar.Shorten_link;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import ir.iamnovinfar.Shorten_link.Activity.OtpCheckActivity;

public class OTP_Receiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {



        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    String data=messages[0].getMessageBody();

                    if (data.contains("سلام \n" + "کد شما برای ورود به برنامه")){
                        String data1 = data.substring(data.indexOf(":") + 1, data.lastIndexOf("ل"));
                        Log.i(TAG, data1);
                        OtpCheckActivity.edt_auth_otp.setText(data1.trim());

                    }
                }
            }
        }

    }
}