package com.example.intaketracker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class recipedisplay : AppCompatActivity() {

    private lateinit var imagge1:ImageView
    private lateinit var recipename:TextView
    private lateinit var cal:TextView
    private lateinit var ing:TextView
    private lateinit var dir:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipedisplay)

        imagge1=findViewById(R.id.Image)
        recipename=findViewById(R.id.idrecipeName)
        cal=findViewById(R.id.idcal)
        ing=findViewById(R.id.ingredients)
        dir=findViewById(R.id.directions)

     val k = admin_DBHelper(this)
        val recipeName = intent.getStringExtra("recipeName")
        val recipeImageByteArray = intent.getByteArrayExtra("recipeImage")
        val calories = intent.getStringExtra("calories")
        recipename.text=recipeName
        cal.text=calories
        if (recipeImageByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(recipeImageByteArray, 0, recipeImageByteArray.size)
            imagge1.setImageBitmap(bitmap)
        }
        val r=recipename.text.toString()
        // Assuming you have already initialized ing and dir TextView widgets
        val recipeDetails = k.getRecipeDetails(r)
// Set ingredients and directions in the respective TextViews
        ing.text = recipeDetails.first.joinToString("\n") // Join ingredients with newlines
        dir.text = recipeDetails.second.joinToString("\n") // Join directions with newlines

    }
}