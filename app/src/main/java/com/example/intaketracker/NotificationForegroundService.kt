//package com.example.intaketracker
//
//import android.app.Notification
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.IBinder
//import android.os.PowerManager
//import androidx.core.app.NotificationCompat
//
//class NotificationForegroundService : Service() {
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val category = intent?.getStringExtra("category")
//        val message = intent?.getStringExtra("message")
//        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag")
//        wakeLock.acquire(10 * 60 * 1000L /* 10 minutes */)
//
//        if (category != null && message != null) {
//            // Create and show your daily notification here using NotificationCompat.Builder
//            val notification = createDailyNotification(category, message)
//
//            // Start the foreground service with the notification
//            startForeground(FOREGROUND_NOTIFICATION_ID, notification)
//        }
//
//        wakeLock.release()
//        return START_STICKY
//    }
//
//    private fun createDailyNotification(category: String, message: String): Notification {
//        val pattern = longArrayOf(0, 100, 200, 100)
//        // Create and customize your daily notification using NotificationCompat.Builder
//        val notificationBuilder = NotificationCompat.Builder(this, channelID)
//            .setSmallIcon(R.drawable.app123456)
//            .setContentTitle(category)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // Add sound
//            .setVibrate(pattern) // Customize vibration as needed
//
//        return notificationBuilder.build()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Send a broadcast to restart the service
//        val broadcastIntent = Intent("RESTART_SERVICE_ACTION")
//        sendBroadcast(broadcastIntent)
//    }
//
//    companion object {
//         const val FOREGROUND_NOTIFICATION_ID = 101
//    }
//}
