package com.amanullah.chromaticsaiassessment.base.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.amanullah.chromaticsaiassessment.presentation.MainActivity

class IncomingCallReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            // Incoming call is ringing, get the number
            val incomingCallerNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            incomingCallerNumber?.let {
                // Start your activity when an incoming call is detected
                val intent1 = Intent(context, MainActivity::class.java)
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent1.putExtra(
                    "number",
                    incomingCallerNumber
                ) // Pass the phone number to the activity
                context.startActivity(intent1)
            }
        } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
            // Call ended or idle, reset the number
            val intent1 = Intent(context, MainActivity::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent1.putExtra("number", "")
            context.startActivity(intent1)
        }
    }
}