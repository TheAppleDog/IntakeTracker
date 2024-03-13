package com.example.intaketracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Macro : AppCompatActivity() {

    private lateinit var ageEditText: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var activityLevelSpinner: Spinner
    private lateinit var goalSpinner: Spinner
    private lateinit var calculateButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macro)

        // Initialize UI components
        ageEditText = findViewById(R.id.editTextAge)
        genderSpinner = findViewById(R.id.spinnergender)
        heightEditText = findViewById(R.id.editTextHeight)
        weightEditText = findViewById(R.id.editTextWeight)
        activityLevelSpinner = findViewById(R.id.spinnerActivityLevel)
        goalSpinner = findViewById(R.id.spinnergoal)
        calculateButton = findViewById(R.id.calculateButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Initialize spinners with data from resources (arrays)
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.genders,
            R.layout.spinner_custom
        )
        genderAdapter.setDropDownViewResource(R.layout.spinner_custom)
        genderSpinner.adapter = genderAdapter

        val activityLevelAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.activity_levels,
            R.layout.spinner_custom
        )
        activityLevelAdapter.setDropDownViewResource(R.layout.spinner_custom)
        activityLevelSpinner.adapter = activityLevelAdapter

        val goalAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.goals,
            R.layout.spinner_custom
        )
        goalAdapter.setDropDownViewResource(R.layout.spinner_custom)
        goalSpinner.adapter = goalAdapter

        // Set a click listener for the calculate button
        calculateButton.setOnClickListener { calculateMacros() }
    }

    private fun calculateMacros() {
        // Retrieve user input from EditText and Spinner views
        val age = ageEditText.text.toString().toIntOrNull() ?: 0
        val genderId = genderSpinner.selectedItemPosition
        val height = heightEditText.text.toString().toDoubleOrNull() ?: 0.0
        val weight = weightEditText.text.toString().toDoubleOrNull() ?: 0.0
        val activityLevel = activityLevelSpinner.selectedItem.toString()
        val goal = goalSpinner.selectedItem.toString()

        // Calculate BMR using the Mifflin-St Jeor Equation
        val bmr = calculateBMR(age, genderId, height, weight)

        // Adjust BMR based on activity level
        val tdee = calculateTDEE(bmr, activityLevel)

        // Adjust macros based on goals and body fat percentage
        val macros = calculateMacros(tdee, goal,weight)

        // Display the result in the TextView
        val formattedProtein = String.format("%.2f", macros.carbs)
        val formattedCarbs = String.format("%.2f", macros.protein)
        val formattedFat = String.format("%.2f", macros.fat)

        val resultText = "Carbs : $formattedCarbs gms\nProtein : $formattedProtein gms\nFat : $formattedFat gms"
        resultTextView.text = resultText

    }

    private fun calculateBMR(age: Int, genderId: Int, height: Double, weight: Double): Double {
        // Implement the BMR calculation logic here (Mifflin-St Jeor Equation)
        val bmr: Double
        if (genderId == 0) { // Male
            bmr = 10 * weight + 6.25 * height - 5 * age + 5
        } else { // Female
            bmr = 10 * weight + 6.25 * height - 5 * age - 161
        }
        return bmr
    }

    private fun calculateTDEE(bmr: Double, activityLevel: String): Double {
        // Implement the TDEE calculation logic here (based on activity level)
        val tdee: Double
        when (activityLevel) {
            "Sedentary" -> tdee = bmr * 1.2
            "Lightly Active" -> tdee = bmr * 1.375
            "Moderately Active" -> tdee = bmr * 1.55
            "Very Active" -> tdee = bmr * 1.725
            else -> tdee = bmr * 1.9
        }
        return tdee
    }

    private fun calculateMacros(tdee: Double, goal: String, weight: Double): Macros {
        var protein: Double
        val carbs: Double
        val fat: Double

        // Adjust macros based on goals (e.g., fat loss, maintenance, muscle gain)
        when (goal) {
            "Fat Loss" -> {
                protein = 1.2 * weight // 1.2g of protein per pound of body weight
                carbs = 0.4 * tdee / 4 // 40% of TDEE from carbs
                fat = 0.3 * tdee / 9 // 30% of TDEE from fat
            }
            "Maintenance" -> {
                protein = 1.0 * weight // 1.0g of protein per pound of body weight
                carbs = 0.5 * tdee / 4 // 50% of TDEE from carbs
                fat = 0.3 * tdee / 9 // 30% of TDEE from fat
            }
            "Muscle Gain" -> {
                protein = 1.2 * weight // 1.2g of protein per pound of body weight
                carbs = 0.6 * tdee / 4 // 60% of TDEE from carbs
                fat = 0.25 * tdee / 9 // 25% of TDEE from fat
            }
            else -> {
                protein = 1.0 * weight // Default: 1.0g of protein per pound of body weight
                carbs = 0.5 * tdee / 4 // Default: 50% of TDEE from carbs
                fat = 0.3 * tdee / 9 // Default: 30% of TDEE from fat
            }
        }
        return Macros(carbs, protein, fat)
    }
    data class Macros(val protein: Double, val carbs: Double, val fat: Double)
}
