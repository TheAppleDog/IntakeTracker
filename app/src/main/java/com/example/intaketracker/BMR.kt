package com.example.intaketracker

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class BMR : AppCompatActivity() {
    private lateinit var editTextAge: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var editTextWeight: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var calculateButton: Button
    private lateinit var textViewResult: TextView
    private lateinit var info: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmr)

        editTextAge = findViewById(R.id.editTextAge)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextHeight = findViewById(R.id.editTextHeight)
        calculateButton = findViewById(R.id.buttonCalculate)
        textViewResult = findViewById(R.id.textViewResult)
        info = findViewById(R.id.info)

        info.setOnClickListener{
            val j = Intent(this,infobmr::class.java)
            startActivity(j)
        }

        calculateButton.setOnClickListener {
            calculateBMR()
        }
    }

    private fun calculateBMR() {
        val age = editTextAge.text.toString().toIntOrNull() ?: 0
        val weightKg = editTextWeight.text.toString().toFloatOrNull() ?: 0f
        val heightCm = editTextHeight.text.toString().toFloatOrNull() ?: 0f

        val gender = when (radioGroupGender.checkedRadioButtonId) {
            R.id.radioButtonMale -> "male"
            R.id.radioButtonFemale -> "female"
            else -> "male" // Default to male if not selected
        }

        // Calculate BMR based on age, gender, weight, and height
        val bmr = calculateBasalMetabolicRate(gender, age, weightKg, heightCm)

        // Display the result
        textViewResult.text = getString(R.string.yourbmrcaloriesday, bmr.roundToInt())
    }

    private fun calculateBasalMetabolicRate(gender: String, age: Int, weightKg: Float, heightCm: Float): Float {
        return if (gender == "male") {
            (88.362f + (13.397f * weightKg) + (4.799f * heightCm) - (5.677f * age))
        } else {
            (447.593f + (9.247f * weightKg) + (3.098f * heightCm) - (4.330f * age))
        }
    }
}