package com.example.intaketracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BMI : AppCompatActivity() {
    private lateinit var editTextWeight: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var editTextAge: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        // Initialize UI elements
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextAge = findViewById(R.id.editTextAge)
        spinnerGender = findViewById(R.id.spinnerGender)
        textViewResult = findViewById(R.id.textViewResult)

        // Populate the gender spinner with options (e.g., "Male" and "Female")
        val genderOptions = arrayOf("Male", "Female")
        val adapter = ArrayAdapter(this, R.layout.spinner_custom, genderOptions)
        spinnerGender.adapter = adapter

        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        buttonCalculate.setOnClickListener {
            calculateBMI()
        }
    }

    private fun calculateBMI() {
        // Retrieve weight, height, age, and gender values from UI
        val weightStr = editTextWeight.text.toString()
        val heightStr = editTextHeight.text.toString()
        val ageStr = editTextAge.text.toString()
        spinnerGender.selectedItem.toString()

        // Convert input values to appropriate data types
        val weight = weightStr.toDoubleOrNull()
        val height = heightStr.toDoubleOrNull()
        val age = ageStr.toIntOrNull()

        // Check if any input is empty or invalid
        if (weight == null || height == null || age == null) {
            textViewResult.text = getString(R.string.invalid)
            return
        }

        // Calculate BMI
        val bmi = calculateBMI(weight, height)
        // Display the result
        // Retrieve the formatted string resource and set it to the TextView
        val formattedBMI =  "Your BMI : %.2f".format(bmi)
        textViewResult.text = formattedBMI
        // You can add further logic to interpret and classify BMI values
        // based on age, gender, and other factors.
    }
    private fun calculateBMI(weight: Double, height: Double): Double {
        // Calculate BMI using weight (in kilograms) and height (in meters)

        return weight / (height * height)
    }
}
