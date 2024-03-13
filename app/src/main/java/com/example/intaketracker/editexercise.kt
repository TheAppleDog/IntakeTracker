package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class editexercise : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var edittext: EditText
    private lateinit var editButton: Button
    private lateinit var updateButton: Button

    private lateinit var dbHelper: admin_DBHelper
    private var selectedExercise: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editexercise)

        // Initialize UI elements
        spinner = findViewById(R.id.deptSpinner)
        edittext = findViewById(R.id.edtDept)
        editButton = findViewById(R.id.btnedit)
        updateButton = findViewById(R.id.btndedit)

        // Create an instance of your DBHelper
        dbHelper = admin_DBHelper(this)

        // Fetch all food names from the database
        val exNames = dbHelper.getAllExercise()

        // Create an ArrayAdapter to populate the spinner with food names
        val adapter = ArrayAdapter(this, R.layout.spinner_custom, exNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected food name and display it in the EditText for editing
                selectedExercise = exNames[position]
                val calories = dbHelper.getMetForExercise(selectedExercise)
                edittext.setText(calories.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
        // Handle the "Edit" button click
        editButton.setOnClickListener {
            // Set the selected food name from the spinner to the EditText
            edittext.setText(selectedExercise)
        }
        // Handle the "Update Calories" button click
        updateButton.setOnClickListener {
            val newMet = edittext.text.toString().toDoubleOrNull()

            if (newMet != null) {
                // Update the calories for the selected food name in the database
                val rowsAffected = dbHelper.updateMet(selectedExercise, newMet)
                val intent = Intent(this, navigateto::class.java)
                startActivity(intent)
                finish()
                if (rowsAffected > 0) {
                    // Display a toast message indicating the calories were updated
                    Toast.makeText(
                        this,
                        "Calories for '$selectedExercise' updated to '$newMet' in the database",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Optionally, you can update the Spinner with the new data from the database
                    val updatedExerciseNames = dbHelper.getAllExercise()
                    adapter.clear()
                    adapter.addAll(updatedExerciseNames)
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle the case where the update fails
                    Toast.makeText(this, "Failed to update MET", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle the case where an invalid value is entered
                Toast.makeText(this, "Please enter a valid MET value", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
