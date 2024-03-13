//package com.example.intaketracker
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//
//class RestartServiceReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent?.action == "RESTART_SERVICE_ACTION") {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                // Start the foreground service again
//                val serviceIntent = Intent(context, NotificationForegroundService::class.java)
//                context?.startForegroundService(serviceIntent)
//            }
//        }
//    }
//}
