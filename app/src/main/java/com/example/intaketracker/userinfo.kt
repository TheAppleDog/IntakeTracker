package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.hdodenhof.circleimageview.CircleImageView

class userinfo : AppCompatActivity() {
    private var selectedgender: String? = null
    private lateinit var sessionManagement: session
    private lateinit var circularGraphView: CircularGraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)
        val age = findViewById<EditText>(R.id.age)
        val weight = findViewById<EditText>(R.id.weight)
        val height = findViewById<EditText>(R.id.height)
        val male = findViewById<CircleImageView>(R.id.male)
        val female = findViewById<CircleImageView>(R.id.female)
        val cal = findViewById<EditText>(R.id.cal)
        val next = findViewById<Button>(R.id.btn)
        val msg = findViewById<TextView>(R.id.usr)
        val g = DBHelper(this)
        sessionManagement = session(this)

        val message = intent.getStringExtra("message_key")
        msg.text = message

        // Find the DashboardFragment
        val dashboardFragment = supportFragmentManager.findFragmentByTag("dashboardFragment") as DashboardFragment?

        // Find the CircularGraphView within the DashboardFragment's layout
        circularGraphView = dashboardFragment?.view?.findViewById(R.id.caloriesGraph) ?: CircularGraphView(this, null)

        male.setOnClickListener {
            selectedgender = "male"
            male.setBorderColor(ContextCompat.getColor(this, R.color.teal_200))
            female.setBorderColor(ContextCompat.getColor(this, R.color.border))
        }

        female.setOnClickListener {
            selectedgender = "female"
            male.setBorderColor(ContextCompat.getColor(this, R.color.border))
            female.setBorderColor(ContextCompat.getColor(this, R.color.teal_200))
        }

        next.setOnClickListener {
            val usr = msg.text.toString()
            val ages = age.text.toString()
            val weights = weight.text.toString()
            val heights = height.text.toString()
            val maxCaloriesStr = cal.text.toString()
            Log.d("userinfo", "Max Calories Input: $maxCaloriesStr")
            if (ages == "" || weights == "" || heights == "" || maxCaloriesStr == "") {
                Toast.makeText(applicationContext, "ENTER ALL FIELDS.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    // Convert the maxCalories input to an integer
                    g.insertuserprofile(usr, selectedgender!!, ages, weights, heights, maxCaloriesStr)
                    Toast.makeText(applicationContext, "Details Recorded", Toast.LENGTH_SHORT).show()
// Pass maxCalories value to DashboardFragment
                    val diaryFragment = supportFragmentManager.findFragmentByTag("diaryFragment") as DiaryFragment?
                    val bundle = Bundle()
                    val Cal=maxCaloriesStr.toInt()
                    bundle.putInt("maxCalories", Cal)
                    diaryFragment?.arguments = bundle
                    val intent = Intent(application, dashboard::class.java)
                    intent.putExtra("message_key", usr) // Pass the username
                    startActivity(intent)
                    finish()
                } catch (_: Exception) {

                }
            }
        }
    }
}
