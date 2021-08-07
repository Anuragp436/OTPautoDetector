package com.example.otpcatcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import android.widget.EditText

class OTPdetector:BroadcastReceiver() {
    companion object {
        public lateinit var editText: EditText
    }
    public fun OTP(et: EditText) {
        editText = et
        Log.d("text", "$editText")
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            for(i in Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                val mb=i.messageBody
                Log.d("ottp", mb)
                val otp=mb.split(" ")[0]
                Log.d("OTTP", otp)
                editText.setText(otp)
            }
        }
    }
}