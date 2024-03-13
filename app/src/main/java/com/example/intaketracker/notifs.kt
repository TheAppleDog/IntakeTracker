//package com.example.intaketracker
//
//import android.app.*
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.SwitchCompat
//import com.example.intaketracker.databinding.ActivityNotifsBinding
//import java.util.Calendar
//import java.util.Random
//
//class notifs : AppCompatActivity() {
//
//    private lateinit var allowSwitch: SwitchCompat
//    private lateinit var switchMap: Map<String, SwitchCompat>
//    private lateinit var timeTextViewMap: Map<String, TextView>
//    private val sharedPreferencesKey = "IntakeTrackerPrefs"
//    private lateinit var binding: ActivityNotifsBinding
//
//    private fun startForegroundService() {
//        val serviceIntent = Intent(this, NotificationForegroundService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(serviceIntent)
//        } else {
//            startService(serviceIntent)
//        }
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNotifsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize Switches and TextViews
//        allowSwitch = binding.switch1
//        switchMap = mapOf(
//            "Breakfast" to binding.switchbreakfast,
//            "Lunch" to binding.switchlunch,
//            "Snacks" to binding.switchsnacks,
//            "Dinner" to binding.switchdinner,
//            "Exercise" to binding.switchexercise,
////            "Water" to binding.switchwater
//        )
//        timeTextViewMap = mapOf(
//            "Breakfast" to binding.bftimepicker,
//            "Lunch" to binding.lunchtimepicker,
//            "Snacks" to binding.snackstimepicker,
//            "Dinner" to binding.dinetimepicker,
//            "Exercise" to binding.extimepicker,
////            "Water" to binding.watertimepicker
//        )
//
//        // Set click listeners for switches and TextViews
//        setSwitchListeners()
//        setTextViewClickListeners()
//
//        // Load saved notification times and switch states
//        loadNotificationTimes()
//        loadAllowSwitchState()
//
//        // Create a notification channel
//        createNotificationChannel()
////        if (isAnySwitchEnabled()) {
////            startForegroundService()
////        }
//    }
////    private fun isAnySwitchEnabled(): Boolean {
////        return switchMap.values.any { switch ->
////            switch.isChecked
////        }
////    }
//    private fun setSwitchListeners() {
//        allowSwitch.setOnCheckedChangeListener { _, isChecked ->
//            // Save the allow switch state to SharedPreferences
//            saveAllowSwitchState(isChecked)
//            // Enable or disable all category switches based on the allow switch state
//            switchMap.values.forEach { switch ->
//                switch.isEnabled = isChecked
//            }
//            // Show or hide the linear layout containing switches based on the allow switch state
//            binding.ll.visibility = if (isChecked) View.VISIBLE else View.GONE
//        }
//        switchMap.forEach { (category, switch) ->
//            switch.setOnCheckedChangeListener { _, isChecked ->
//                // Save the category switch state to SharedPreferences
//                saveCategorySwitchState(category, isChecked)
//            }
//        }
//    }
//    private fun saveCategorySwitchState(category: String, isChecked: Boolean) {
//        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putBoolean(category + "_switch", isChecked)
//        editor.apply()
//    }
//    private fun setTextViewClickListeners() {
//        timeTextViewMap.forEach { (category, textView) ->
//            textView.setOnClickListener {
//                showTimePicker(textView, category)
//            }
//        }
//    }
//    private fun showTimePicker(textView: TextView, category: String) {
//        val currentTime = Calendar.getInstance()
//        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
//        val minute = currentTime.get(Calendar.MINUTE)
//        val timePickerDialog = TimePickerDialog(
//            this,
//            { _, selectedHour, selectedMinute ->
//                val isAM: Boolean = selectedHour < 12
//                val amPm = if (isAM) "AM" else "PM"
//                val formattedTime = String.format(
//                    "%02d:%02d %s",
//                    if (selectedHour > 12) selectedHour - 12 else selectedHour,
//                    selectedMinute,
//                    amPm
//                )
//                textView.text = formattedTime
//                saveNotificationTime(category, formattedTime) // Save the updated time
//            },
//            hour,
//            minute,
//            true
//        )
//        timePickerDialog.show()
//    }
//
//
////    private fun showWaterIntervalPicker() {
////        val intervals = resources.getStringArray(R.array.water_intervals)
////
////        AlertDialog.Builder(this)
////            .setTitle("Select Water Interval")
////            .setItems(intervals) { dialogInterface: DialogInterface, which: Int ->
////                val selectedInterval = intervals[which]
////                binding.watertimepicker.text = selectedInterval
////
////                // Save the selected water interval to SharedPreferences
////                saveWaterInterval(which)
////
////                dialogInterface.dismiss()
////
////                // Schedule water notifications based on the selected interval
////                scheduleWaterNotifications(which)
////            }
////            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _ ->
////                dialogInterface.dismiss()
////            }
////            .show()
////    }
////    private fun saveWaterInterval(intervalIndex: Int) {
////        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
////        val editor = prefs.edit()
////        editor.putInt("waterInterval", intervalIndex)
////        editor.apply()
////    }
//
////    private fun scheduleWaterNotifications(category: String,intervalIndex: Int) {
////        // Calculate the time interval in milliseconds based on the selected interval
////        val intervalMillis = (intervalIndex + 1) * 60 * 60 * 1000 // Convert hours to milliseconds
////        val message = getMessageForCategory(category)
////        // Schedule water notifications at the specified interval
////        val notificationId = 0 // You can use a unique ID for water notifications
////        val requestCode = getCategoryIndex(category)
////        val intent = Intent(this, NotificationReceiver::class.java)
////        intent.putExtra("category", category)
////        intent.putExtra("message", message)
////        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
////        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
////        alarmManager.setRepeating(
////            AlarmManager.RTC_WAKEUP,
////            System.currentTimeMillis() + intervalMillis, // Schedule the first notification after the interval
////            intervalMillis.toLong(),
////            pendingIntent
////        )
////    }
//    private fun loadNotificationTimes() {
//        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//        timeTextViewMap.forEach { (category, textView) ->
//            if (prefs.contains(category)) {
//                val savedTime = prefs.getString(category, "")
//                if (!savedTime.isNullOrEmpty()) {
//                    textView.text = savedTime
//                    // Schedule a notification for the saved time
//                    scheduleNotification(category, savedTime)
//                }
//            }
//        }
//    }
//    private fun saveNotificationTime(category: String, time: String) {
//        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putString(category, time)
//        editor.apply()
//    }
//    private fun loadAllowSwitchState() {
//        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//        val isAllowSwitchChecked = prefs.getBoolean("isAllowSwitchChecked", false)
//        allowSwitch.isChecked = isAllowSwitchChecked
//
//        // Set the visibility of the linear layout based on the switch state
//        binding.ll.visibility = if (isAllowSwitchChecked) View.VISIBLE else View.GONE
//
//        // Load and set the states of individual switches
//        switchMap.forEach { (category, switch) ->
//            if (prefs.contains(category + "_switch")) {
//                switch.isChecked = prefs.getBoolean(category + "_switch", false)
//            }
//        }
//    }
//    private fun saveAllowSwitchState(isChecked: Boolean) {
//        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putBoolean("isAllowSwitchChecked", isChecked)
//        editor.apply()
//    }
//
//    private fun scheduleNotification(category: String, formattedTime: String) {
//        val message = getMessageForCategory(category)
//        val timeParts = formattedTime.split(" ")
//        val time = timeParts[0]
//        val amPm = timeParts[1]
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MILLISECOND, 0)
//        val hourOfDay = when {
//            amPm.equals("PM", ignoreCase = true) -> {
//                val hour = time.split(":")[0].toInt()
//                if (hour < 12) hour + 12 else hour
//            }
//
//            else -> time.split(":")[0].toInt()
//        }
//
//        val minute = time.split(":")[1].toInt()
//
//        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//        calendar.set(Calendar.MINUTE, minute)
//
//        // Generate a unique notification ID based on category
//        val requestCode = getCategoryIndex(category)
//        val intent = Intent(this, NotificationReceiver::class.java)
//        intent.putExtra("category", category)
//        intent.putExtra("message", message)
//        val pendingIntent =
//            PendingIntent.getBroadcast(
//                this,
//                requestCode,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Use setExactAndAllowWhileIdle for API 23 and above
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
//        } else {
//            alarmManager.set(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//        }
//    }
//
//    private fun getCategoryIndex(category: String): Int {
//        val categories = listOf(
//            "Breakfast",
//            "Lunch",
//            "Snacks",
//            "Dinner",
//            "Exercise",
////            "Water"
//        )
//        return categories.indexOf(category)
//    }
//
//    private fun getMessageForCategory(category: String): String {
//        return when (category) {
//            "Breakfast" -> getRandomBreakfastMessage()
//            "Lunch" -> getRandomLunchMessage()
//            "Snacks" -> getRandomSnacksMessage()
//            "Dinner" -> getRandomDinnerMessage()
//            "Exercise" -> getRandomExerciseMessage()
////            "Water" -> getRandomWaterMessage()
//            else -> ""
//        }
//    }
//
//    private fun getRandomBreakfastMessage(): String {
//        val breakfastMessages = listOf(
//            "Start your day with a nutritious breakfast!",
//            "Rise and shine! Time for a delicious breakfast.",
//            "Don't skip the most important meal of the day – breakfast!",
//            "Fuel up your morning with a hearty breakfast.",
//            "Enjoy a healthy breakfast to kickstart your day.",
//            "Good morning! How about some breakfast goodness?"
//        )
//        return getRandomMessage(breakfastMessages)
//    }
//
//    private fun getRandomLunchMessage(): String {
//        val lunchMessages = listOf(
//            "Lunchtime! Refuel and recharge.",
//            "Take a break and enjoy your lunch.",
//            "A tasty lunch awaits you!",
//            "Time to savor your midday meal.",
//            "Lunch hour is the best hour!",
//            "Delight in your lunch break!"
//        )
//        return getRandomMessage(lunchMessages)
//    }
//
//    private fun getRandomSnacksMessage(): String {
//        val snacksMessages = listOf(
//            "Snack attack! Grab a healthy bite.",
//            "Elevate your energy with a quick snack.",
//            "Munch on something delightful!",
//            "Satisfy your cravings with a tasty snack.",
//            "Snacking made better – enjoy!",
//            "A little snack never hurts!"
//        )
//        return getRandomMessage(snacksMessages)
//    }
//
//    private fun getRandomDinnerMessage(): String {
//        val dinnerMessages = listOf(
//            "Dinner is served! Bon appétit!",
//            "End your day with a hearty dinner.",
//            "Relax and enjoy a delicious dinner.",
//            "A flavorful dinner awaits you.",
//            "Savor the flavors of your evening meal.",
//            "Dinnertime – the best time!"
//        )
//        return getRandomMessage(dinnerMessages)
//    }
//
//    private fun getRandomExerciseMessage(): String {
//        val exerciseMessages = listOf(
//            "Time to get moving! Let's exercise.",
//            "Exercise for a healthier you!",
//            "Workout mode activated!",
//            "Get active and stay fit.",
//            "Sweat it out with some exercise.",
//            "Ready, set, exercise!"
//        )
//        return getRandomMessage(exerciseMessages)
//    }
//
////    private fun getRandomWaterMessage(): String {
////        val waterMessages = listOf(
////            "Stay hydrated! It's time to drink water.",
////            "Water break reminder – drink up!",
////            "Your body needs water – stay refreshed.",
////            "Don't forget to hydrate throughout the day.",
////            "Keep the water flowing for a healthy you.",
////            "Cheers to good hydration!"
////        )
////        return getRandomMessage(waterMessages)
////    }
//
//    private fun getRandomMessage(messages: List<String>): String {
//        val random = Random()
//        return messages[random.nextInt(messages.size)]
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Intake Tracker"
//            val descriptionText = "Notification Channel for Intake Tracker"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(
//                channelID,
//                name,
//                importance
//            ).apply {
//                description = descriptionText
//            }
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}
