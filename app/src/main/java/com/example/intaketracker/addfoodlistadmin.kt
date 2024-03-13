package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class addfoodlistadmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfoodlistadmin)

        val food_name = findViewById<EditText>(R.id.foodNameEditText)
        val food_cal = findViewById<EditText>(R.id.foodcalories)
        val servesize = findViewById<EditText>(R.id.servingSize)
        val add = findViewById<Button>(R.id.addButton)
        val k = admin_DBHelper(this)

        add.setOnClickListener{
            val fname=food_name.text.toString()
            val fcal=food_cal.text.toString()
            val serveS=servesize.text.toString()
            if (fname.isEmpty() && fcal.isEmpty() && serveS.isEmpty()) {
                Toast.makeText(applicationContext, " \n ENTER FIELDS", Toast.LENGTH_LONG)
                    .show()
            }
                else{
                    k.insertfoodlist(fname,serveS,fcal.toInt())
                    Toast.makeText(applicationContext, "$fname $fcal Added", Toast.LENGTH_SHORT)
                        .show()
                val intent = Intent(this,navigateto::class.java)
                startActivity(intent)
                finish()
                }
        }

    }
}