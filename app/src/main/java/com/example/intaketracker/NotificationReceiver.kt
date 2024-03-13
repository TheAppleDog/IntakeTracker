//package com.example.intaketracker
//
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Vibrator
//import androidx.core.app.NotificationCompat
//import java.util.Random
//const val notificationID = 1
//const val channelID = "channel1"
//class NotificationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val category = intent?.getStringExtra("category")
//        val message = intent?.getStringExtra("message")
//
//        if (category != null && message != null && context != null) {
//            val notificationId = generateUniqueNotificationId()
//
//            val notification = NotificationCompat.Builder(context, channelID)
//                .setSmallIcon(R.drawable.app123456)
//                .setContentTitle(category)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .build()
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.notify(notificationId, notification)
//            // Vibrate the device
//            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//            val pattern = longArrayOf(0, 100, 200, 100) // Vibrate pattern (0ms delay, 100ms vibrate, 200ms pause, 300ms vibrate)
//            vibrator.vibrate(pattern, -1) // -1 means do not repeat
//        }
//    }
//
//    private fun generateUniqueNotificationId(): Int {
//        // Generate a unique notification ID based on your logic.
//        // You can use a counter, a random number, or any method to ensure uniqueness.
//        // For simplicity, let's use a random number generator.
//        return Random().nextInt(10000) // Adjust the range as needed.
//    }
//}
