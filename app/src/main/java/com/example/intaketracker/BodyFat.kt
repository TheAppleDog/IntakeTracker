package com.example.intaketracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class BodyFat : AppCompatActivity() {
    private lateinit var editTextAge: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var editTextWeight: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var calculateButton: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_fat)

// Initialize UI elements (including the new ones)
        editTextAge = findViewById(R.id.editTextAge)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextHeight = findViewById(R.id.editTextHeight)
        calculateButton = findViewById(R.id.calculateButton)
        textViewResult = findViewById(R.id.textViewResult)
        calculateButton.setOnClickListener {
            calculateBodyFat()
        }
    }

    private fun calculateBodyFat() {
        val age = editTextAge.text.toString().toIntOrNull() ?: 0
        val weightKg = editTextWeight.text.toString().toFloatOrNull() ?: 0f
        val heightCm = editTextHeight.text.toString().toFloatOrNull() ?: 0f
        // Calculate BMI (Body Mass Index)
        val bmi = calculateBMI(weightKg, heightCm)

        // Calculate body fat based on age, gender, BMI, neck, and waist measurements
        val isMale = radioButtonMale.isChecked
        val bodyFatPercentage = calculateBodyFatPercentage(isMale, age, bmi)

        // Display the result
        val resultText = "Your Body Fat: ${bodyFatPercentage.roundToInt()}%"
        textViewResult.text = resultText
    }

    // Existing calculateBMI function remains the same
    private fun calculateBMI(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100
        return weightKg / (heightM * heightM)
    }

    private fun calculateBodyFatPercentage(
        isMale: Boolean,
        age: Int,
        bmi: Float
    ): Float {
        val factor = if (isMale) {
            1.20f
        } else {
            1.20f
        }

        val bfp = factor * bmi + 0.23f * age - if (isMale) 16.2f else 5.4f

        return bfp
    }

}