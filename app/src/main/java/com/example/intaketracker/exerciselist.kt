package com.example.intaketracker

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class exerciselist : AppCompatActivity() {

    private lateinit var exl: ListView
    private lateinit var k: admin_DBHelper
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exerciselist)

        exl=findViewById(R.id.l)
        k = admin_DBHelper(this)
        k.insertexerciselist("Crunches",2.8)
        k.insertexerciselist("Leg Raise",2.5)
        k.insertexerciselist("Jogging",7.0)
        k.insertexerciselist("Bicycling 16-19km/h",5.0)
        k.insertexerciselist("Bicycling 19-22.5km/h",5.0)
        k.insertexerciselist("Bicycling over 22.5km/h",5.0)
        k.insertexerciselist("Back extension",3.5)
        k.insertexerciselist("Barbell press",5.0)
        k.insertexerciselist("Chest press",5.5)
        k.insertexerciselist("Biceps curl",4.0)
        k.insertexerciselist("Calf raises",3.5)
        k.insertexerciselist("Deadlift",5.5)
        k.insertexerciselist("Dips", 4.5)
        k.insertexerciselist("Lat pull down",4.0)
        k.insertexerciselist("Leg curls",2.5)
        k.insertexerciselist("Leg extension",3.4)
        k.insertexerciselist("Leg press",4.5)
        k.insertexerciselist("Lunges",3.0)
        k.insertexerciselist("Machine squat",4.0)
        k.insertexerciselist("Pull ups",4.5)
        k.insertexerciselist("Push ups",3.5)
        k.insertexerciselist("Running 5mph",8.0)
        k.insertexerciselist("Running 6mph",9.8)
        k.insertexerciselist("Running 7.5mph",11.0)
        k.insertexerciselist("Running 9mph",12.8)
        k.insertexerciselist("Sprinting 10mph or faster",16.5)
        k.insertexerciselist("Shoulder press",5.0)
        k.insertexerciselist("Squat",3.5)
        k.insertexerciselist("Tricep extension",3.0)
        k.insertexerciselist("Wrist curl",2.5)
        k.insertexerciselist("Wrist Roller",4.5)
        // Sample data for foodList, serving sizes, and initial calories
        val exList = k.getExerciseListNamesAndMET()
        adapter=ArrayAdapter<String>(this,R.layout.custom_list,exList)
        exl.setAdapter(adapter)
        exl.setOnItemClickListener { _, _, position, _ ->
            val selectedExerciseInfo = exList[position]
            val parts = selectedExerciseInfo.split(", ") // Split the string by space
            val selectedExerciseName = parts[0]
            val selectedMETValue = parts[1]

            val intent = Intent(this, exercisecardview::class.java)
            intent.putExtra("ex_name", selectedExerciseName)
            intent.putExtra("met_value", selectedMETValue)
            startActivity(intent)
        }


        // Create a SearchView programmatically
        val searchView = SearchView(this)
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

// Set the top margin (adjust this value as needed)
        layoutParams.topMargin = 15 // Replace with the top margin you want

// Apply the layout parameters to the SearchView
        searchView.layoutParams = layoutParams
        // Set the background color using backgroundTint
        searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSecondary))
        // Customize the SearchView if needed
        searchView.queryHint = "Search Food..."
        searchView.isIconified = false

        // Add the SearchView to your layout
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(searchView)

        // Set up the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search Exercise"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (exList.contains(query)) {
                    adapter.filter.filter(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}