package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class foodcardview : AppCompatActivity() {

    private lateinit var sessionManagement: session
    private lateinit var k: admin_DBHelper
    private lateinit var g: DBHelper
    private lateinit var servingSizeSpinner: Spinner
    private lateinit var mealSpinner: Spinner
    private lateinit var caloriesTextView: TextView
    private lateinit var carbsTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var fatTextView: TextView
    private lateinit var foodname: TextView
    private lateinit var numberOfServingsTextView: TextView
    private lateinit var incrementButton: ImageView
    private lateinit var decrementButton: ImageView
    private lateinit var buttonAdd: Button
    private lateinit var buttonClose: Button
    private lateinit var adp: ArrayAdapter<String>
    private var numberOfServings = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("foodcardview", "onCreate() executed")
        setContentView(R.layout.activity_foodcardview)
        sessionManagement = session(this) // Initialize session management
        k = admin_DBHelper(this)
        g = DBHelper(this)
        servingSizeSpinner = findViewById(R.id.servingSizeSpinner)
        mealSpinner=findViewById(R.id.mealSpinner)
        caloriesTextView = findViewById(R.id.calories)
        carbsTextView=findViewById(R.id.carbs)
        proteinTextView=findViewById(R.id.protein)
        fatTextView=findViewById(R.id.fat)
        numberOfServingsTextView = findViewById(R.id.numberOfServingsTextView)
        incrementButton = findViewById(R.id.incrementButton)
        decrementButton = findViewById(R.id.decrementButton)
        foodname = findViewById(R.id.foodNameTextView)
        buttonAdd = findViewById(R.id.addButton)
        buttonClose = findViewById(R.id.closeButton)
        val mealNames = arrayOf("Breakfast", "Lunch", "Snacks", "Dinner")
        val foodn = intent.getStringExtra("name")
        foodname.text = foodn
        val food=foodname.text.toString()
        servingSizeSpinner.isEnabled=true
        val username = sessionManagement.getUsername()
        val usr=username.toString()
        buttonAdd.setOnClickListener {
            val foodName = foodname.text.toString()
            val servingSize = servingSizeSpinner.selectedItem.toString().toInt()
            val mealN=mealSpinner.selectedItem.toString()
            val noOfServings = numberOfServingsTextView.text.toString().toInt()
            val calories = caloriesTextView.text.toString().toInt()
            val carbs = carbsTextView.text.toString().toInt()
            val protein = proteinTextView.text.toString().toInt()
            val fat = fatTextView.text.toString().toInt()

            g.insertNutritionData(usr,foodName,mealN, servingSize, noOfServings, calories,carbs,protein,fat)
            Toast.makeText(this, "$mealN logged", Toast.LENGTH_SHORT).show()
            // Optionally, you can provide some feedback to the user here.
            // For example, show a toast message indicating the data was inserted successfully.
            // Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
        }
        buttonClose.setOnClickListener{
            val w = Intent(this,foodlist::class.java)
            startActivity(w)
        }
        // Fetch serving sizes from the database
        val servingSizes = k.getServingSizes(food)
        // Split the string by ","
        val servingSizesArray = servingSizes.toString()
            .replace("[", "")  // Remove opening square bracket
            .replace("]", "")  // Remove closing square bracket
            .split(",").map { it.trim() }
// Log the serving size list
        Log.d("ServingSizes", servingSizes.toString())
// Create an ArrayAdapter to populate the spinner
        adp = ArrayAdapter<String>(this, R.layout.spinner_custom, servingSizesArray)
        adp.setDropDownViewResource(R.layout.spinner_custom)
        servingSizeSpinner.setAdapter(adp)


        adp=ArrayAdapter<String>(this,R.layout.spinner_custom,mealNames)
        adp.setDropDownViewResource(R.layout.spinner_custom)
        mealSpinner.setAdapter(adp)
        // Fetch initial calories from the database
        val initialCalories = k.getInitialCalories(food)
        // Set the initial calories to the TextView
        caloriesTextView.text = initialCalories.toString()
        // Set up an OnItemSelectedListener for the spinner
        servingSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateCalories()
                updatecarbs()
                updateprotein()
                updatefat()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
        // Initialize the TextView with default values
        updateCalories()
        updatecarbs()
        updateprotein()
        updatefat()
        incrementButton.setOnClickListener {
            // Increment the number of servings and update the TextView
            numberOfServings++
            numberOfServingsTextView.text = numberOfServings.toString()

            // Update the calories based on the new number of servings
            updateCalories()
            updatecarbs()
            updateprotein()
            updatefat()
        }
        decrementButton.setOnClickListener {
            if (numberOfServings > 1) {
                // Decrement the number of servings and update the TextView
                numberOfServings--
                numberOfServingsTextView.text = numberOfServings.toString()

                // Update the calories based on the new number of servings
                updateCalories()
                updatecarbs()
                updateprotein()
                updatefat()
            }
        }
    }

    private fun updatefat() {
        val selectedFat = calculateFat()
        fatTextView.text = selectedFat.toString()
    }

    private fun calculateFat(): Int {
        foodname.text.toString()
        val fatPercentage = 36.52
        val calories = caloriesTextView.text.toString().toInt()
        return ((calories * fatPercentage) / 100).toInt()
    }

    private fun updateprotein() {
        val selectedProtein = calculateProtein()
        proteinTextView.text = selectedProtein.toString()
    }

    private fun calculateProtein(): Int {
        foodname.text.toString()
        val proteinPercentage = 9.58
        val calories = caloriesTextView.text.toString().toInt()
        return ((calories * proteinPercentage) / 100).toInt()
    }
    private fun updatecarbs() {
        val selectedCarbs = calculateCarbs()
        carbsTextView.text = selectedCarbs.toString()
    }
    private fun calculateCarbs(): Int {
        foodname.text.toString()
        val carbsPercentage = 53.90
        val calories = caloriesTextView.text.toString().toInt()
        return ((calories * carbsPercentage) / 100).toInt()
    }
    private fun updateCalories() {
        val selectedCalories = calculateCalories()
        caloriesTextView.text = selectedCalories.toString()
    }
    private fun calculateCalories(): Int {
        val food = foodname.text.toString()
        val initialCaloriesPerServing = k.getInitialCalories(food)

        // Check if a serving size other than the default (index 0) is selected in the spinner
        val selectedServingSize = servingSizeSpinner.selectedItem.toString()
        if (selectedServingSize.isNotEmpty() && servingSizeSpinner.selectedItemPosition > 0) {
            val totalGrams = selectedServingSize.toInt()
            return (initialCaloriesPerServing * totalGrams / 100) * numberOfServings
        }

        // If the default serving size is selected (index 0), return initialCalories without calculation
        return initialCaloriesPerServing * numberOfServings
    }
}
