package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class addexerciselistadmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addexerciselistadmin)

        val exercise_name = findViewById<EditText>(R.id.exNameEditText)
        val met = findViewById<EditText>(R.id.metvalue)
        val add = findViewById<Button>(R.id.addButton)
        val k = admin_DBHelper(this)

        add.setOnClickListener{
            val exname=exercise_name.text.toString()
            val Met=met.text.toString()
            if (exname.isEmpty() && Met.isEmpty()) {
                Toast.makeText(applicationContext, " \n ENTER FIELDS", Toast.LENGTH_LONG)
                    .show()
            }
            else{
               k.insertexerciselist(exname,Met.toDouble())
                Toast.makeText(applicationContext, "$exname $Met Added", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this,navigateto::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}