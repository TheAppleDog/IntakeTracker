package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class exercisecardview : AppCompatActivity() {
    private lateinit var sessionManagement: session
    private lateinit var exercisename: TextView
    private lateinit var metvalue:TextView
    private lateinit var sets:EditText
    private lateinit var reps:EditText
    private lateinit var duration:EditText
    private lateinit var caloriesburned:TextView
    private var calculatedCalories: Double = 0.0
    private lateinit var add:Button
    private lateinit var cancel:Button
    private lateinit var g:DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercisecardview)

        exercisename = findViewById(R.id.exNameTextView)
        metvalue=findViewById(R.id.metTextView)
        sets=findViewById(R.id.setsTextView)
        reps=findViewById(R.id.repsTextView)
        duration=findViewById(R.id.minsTextView)
        caloriesburned = findViewById<TextView>(R.id.caloriesburnedTextView)
        add=findViewById(R.id.addButton)
        cancel=findViewById(R.id.closeButton)
        sessionManagement = session(this) // Initialize session management
        val username = sessionManagement.getUsername()
        val usr=username.toString()
        g= DBHelper(this)
        val exn = intent.getStringExtra("ex_name")
        exercisename.text = exn
        exercisename.text.toString()
        val met=intent.getStringExtra("met_value")
        metvalue.text=met
        duration.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed, but required to implement TextWatcher
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed, but required to implement TextWatcher
            }
            override fun afterTextChanged(s: Editable?) {
                // Calculate Calories Burned when duration changes
                val metValue = metvalue.text.toString().toDoubleOrNull() ?: 0.0
                val minsValue = s.toString().toDoubleOrNull() ?: 0.0
                val durationInHours = minsValue / 60.0//
                val weight = g.fetchWeightByUsername(usr)
                val weightInKg = weight.toString().toDoubleOrNull() ?: 0.0
                Log.d("CalorieCalculation", "metValue: $metValue")
                Log.d("CalorieCalculation", "minsValue: $minsValue")
                Log.d("CalorieCalculation", "weightInKg: $weightInKg")
                calculatedCalories = (metValue * weightInKg * durationInHours)
                caloriesburned.text = calculatedCalories.toString()
            }
        })

        add.setOnClickListener {
            // Get the values from the UI components
            val exerciseName = exercisename.text.toString()
            val setsValue = sets.text.toString().toInt()
            val repsValue = reps.text.toString().toInt()
            val durationValue = duration.text.toString().toInt()
            // Insert the workout data into the database using the calculated calories
            g.insertWorkout(usr,exerciseName, setsValue, repsValue, durationValue, calculatedCalories)
            Toast.makeText(this, "$exerciseName logged", Toast.LENGTH_SHORT).show()
        }
        cancel.setOnClickListener{
            val w = Intent(this,exerciselist::class.java)
            startActivity(w)
        }
    }
}