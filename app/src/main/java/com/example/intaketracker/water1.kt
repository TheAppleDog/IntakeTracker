package com.example.intaketracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class water1 : AppCompatActivity() {

    private lateinit var bottles: List<ImageView>
    private lateinit var glasses: List<ImageView>
    private lateinit var waterIntakeTextView: TextView
    private lateinit var fillButton: Button
    private var g = DBHelper(this)
    private lateinit var sessionManagement: session
    private var waterIntake: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water1)
        sessionManagement = session(this)

        bottles = listOf(
            findViewById(R.id.bottle1),
            findViewById(R.id.bottle2),
            findViewById(R.id.bottle3)
        )

        glasses = listOf(
            findViewById(R.id.glass1),
            findViewById(R.id.glass2),
            findViewById(R.id.glass3),
            findViewById(R.id.glass4),
            findViewById(R.id.glass5)
        )

        waterIntakeTextView = findViewById(R.id.waterIntakeTextView)
        fillButton = findViewById(R.id.fillButton)

        // Wire click listeners for bottles and glasses
        for (bottle in bottles) {
            bottle.setOnClickListener {
                toggleBottle(bottle)
            }
        }

        for (glass in glasses) {
            glass.setOnClickListener {
                toggleGlass(glass)
            }
        }

        fillButton.setOnClickListener {
            if (fillButton.isEnabled && (isAnyBottleFilled() || isAnyGlassFilled())) {
                // Call the insert function here
                val username = sessionManagement.getUsername()
                val usr = username.toString() // Replace with the actual username
                val waterAmount = waterIntake // Use the total water intake as a Double
                g.insertWaterIntake(usr, waterAmount) // Insert the total water intake as a Double
                Toast.makeText(this, "Water Logged", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No water to save", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun toggleBottle(bottle: ImageView) {
        val bottleCapacity = bottle.tag.toString().toInt()

        val emptyBottleDrawable = ContextCompat.getDrawable(this, R.drawable.empty_bottle)
        val fullBottleDrawable = ContextCompat.getDrawable(this, R.drawable.fill_bottle)

        if (bottle.drawable.constantState == emptyBottleDrawable?.constantState) {
            bottle.setImageDrawable(fullBottleDrawable)
            waterIntake += bottleCapacity.toDouble()
        } else {
            bottle.setImageDrawable(emptyBottleDrawable)
            waterIntake -= bottleCapacity.toDouble()
        }

        // Update the water intake text view here
        updateWaterIntake()
        Log.d("WaterIntake", "Current water intake: $waterIntake")
    }

    private fun toggleGlass(glass: ImageView) {
        val glassCapacity = glass.tag.toString().toInt()

        val emptyGlassDrawable = ContextCompat.getDrawable(this, R.drawable.emptyglass)
        val fullGlassDrawable = ContextCompat.getDrawable(this, R.drawable.fill_glass)

        if (glass.drawable.constantState == emptyGlassDrawable?.constantState) {
            glass.setImageDrawable(fullGlassDrawable)
            waterIntake += glassCapacity.toDouble() / 1000.0
        } else {
            glass.setImageDrawable(emptyGlassDrawable)
            waterIntake -= glassCapacity.toDouble() / 1000.0
        }

        // Update the water intake text view here
        updateWaterIntake()
        Log.d("WaterIntake", "Current water intake: $waterIntake")
    }


    private fun updateWaterIntake() {
        waterIntakeTextView.text = getString(R.string.water_intake_l, waterIntake)
        fillButton.isEnabled = isAnyBottleFilled() || isAnyGlassFilled()
    }

    private fun isAnyBottleFilled(): Boolean {
        for (bottle in bottles) {
            val fullBottleDrawable = ContextCompat.getDrawable(this, R.drawable.fill_bottle)
            if (bottle.drawable.constantState == fullBottleDrawable?.constantState) {
                return true
            }
        }
        return false
    }

    private fun isAnyGlassFilled(): Boolean {
        for (glass in glasses) {
            val fullGlassDrawable = ContextCompat.getDrawable(this, R.drawable.fill_glass)
            if (glass.drawable.constantState == fullGlassDrawable?.constantState) {
                return true
            }
        }
        return false
    }
}
