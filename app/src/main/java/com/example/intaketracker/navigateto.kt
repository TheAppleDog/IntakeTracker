package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class navigateto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigateto)

        val logout = findViewById<Button>(R.id.logout)
        val addfood=findViewById<Button>(R.id.Button)
        val editfoodd=findViewById<Button>(R.id.Button1)
        val delfood=findViewById<Button>(R.id.Button2)
        val addex=findViewById<Button>(R.id.exButton)
        val editex=findViewById<Button>(R.id.exButton1)
        val delex= findViewById<Button>(R.id.exButton2)
        val recipe=findViewById<Button>(R.id.recipe)
        val delrecipe=findViewById<Button>(R.id.recipe1)
        logout.setOnClickListener{
            val intent = Intent(this,login::class.java)
            startActivity(intent)
            finish()
        }
        recipe.setOnClickListener{
            val intent = Intent(this,recipepage1::class.java)
            startActivity(intent)
        }
        addfood.setOnClickListener{
            val intent = Intent(this,addfoodlistadmin::class.java)
            startActivity(intent)
        }
        editfoodd.setOnClickListener{
            val intent = Intent(this,editfood::class.java)
            startActivity(intent)
        }
        delfood.setOnClickListener{
            val intent = Intent(this,deletefood::class.java)
            startActivity(intent)
        }
        addex.setOnClickListener{
            val intent=Intent(this,addexerciselistadmin::class.java)
            startActivity(intent)
        }
        editex.setOnClickListener{
            val intent = Intent(this,editexercise::class.java)
            startActivity(intent)
        }
        delex.setOnClickListener{
            val intent = Intent(this,deleteexercise::class.java)
            startActivity(intent)
        }
        delrecipe.setOnClickListener{
            val intent=Intent(this,deleterecipes::class.java)
            startActivity(intent)
        }
    }
}