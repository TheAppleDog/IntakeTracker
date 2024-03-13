package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class deleteexercise : AppCompatActivity() {

    private lateinit var exSpinner: Spinner
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleteexercise)

        // Initialize views
        exSpinner = findViewById(R.id.exSpinner)
        btnDelete = findViewById(R.id.btndelete)

        // Initialize database (replace "YourDatabaseHelper" with your actual database helper class)
        val k = admin_DBHelper(this)
        // Populate the Spinner with data from the database
        val exNames = k.getAllExercise()
        val adapter = ArrayAdapter(this, R.layout.spinner_custom, exNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exSpinner.adapter = adapter

        // Set an OnItemSelectedListener for the Spinner
        exSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item (you can store it in a variable if needed)
                val selectedFood = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }

        // Set a click listener for the delete button
        btnDelete.setOnClickListener {
            val selectedExercise = exSpinner.selectedItem.toString()
            // Call the deleteFoodByName function here with the selectedFood parameter
            k.deleteExerciseByName(selectedExercise)
            // Refresh the Spinner data after deletion if needed
            val updatedExItems = k.getAllFood()
            adapter.clear()
            adapter.addAll(updatedExItems)
            adapter.notifyDataSetChanged()
            val intent = Intent(this,navigateto::class.java)
            startActivity(intent)
            finish()
        }
    }

}