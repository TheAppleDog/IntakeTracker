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


class foodlist : AppCompatActivity() {

    private lateinit var foodl: ListView
    private lateinit var k: admin_DBHelper
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foodlist)

        foodl=findViewById(R.id.l)
        k = admin_DBHelper(this)
        k.insertfoodlist("Chapati","40",120)
        k.insertfoodlist("Masala Oats","100, 200, 300",87)
        k.insertfoodlist("Fries","100, 200, 300",312)
        k.insertfoodlist("Cheese Burger", "1",420)
        k.insertfoodlist("Plum","1",50)
        k.insertfoodlist("Rice","1",190)
        k.insertfoodlist("Brown Bread","100, 200, 300",313)
        k.insertfoodlist("Eggs","100, 200, 300", 148)
        k.insertfoodlist("Aloo paratha","1",214)
        k.insertfoodlist("Methi paratha","1",260)
        k.insertfoodlist("Omelette","100, 200, 300",154)
        k.insertfoodlist("Whey protein","100, 200, 300",380)
        k.insertfoodlist("Masala Bhindi sabji","100, 200, 300, 400, 500",205)
        k.insertfoodlist("Mutter Paneer","100, 200, 300, 400",190)
        k.insertfoodlist("Soya Bean sabji","100, 200, 300",179)
        // Sample data for foodList, serving sizes, and initial calories
        val foodList= k.getFoodListNames()
        adapter=ArrayAdapter<String>(this,R.layout.spinner_custom,foodList)
        foodl.setAdapter(adapter)
        foodl.setOnItemClickListener { _, _, position, _ ->
            val selectedFoodName = foodList[position] // Get the selected food name from the list
            val k =Intent(this,foodcardview::class.java)
            k.putExtra("name",selectedFoodName)
            startActivity(k)
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
        searchView.queryHint = "Search Food"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (foodList.contains(query)) {
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

