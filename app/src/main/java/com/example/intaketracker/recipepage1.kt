package com.example.intaketracker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class recipepage1 : AppCompatActivity() {
    private lateinit var recipeNameEditText: EditText
    private lateinit var caloriesEditText: EditText
    private lateinit var ingredients:EditText
    private lateinit var directions:EditText
    private lateinit var selectedImageView: ImageView

    private val imagePicker: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null && data.data != null) {
                    val imageUri: Uri = data.data!!
                    // Check the size of the selected image
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val bytes = inputStream?.available() ?: 0
                    // Set a maximum allowed size (e.g., 5MB)
                    val maxFileSizeInBytes = 10 * 1024 * 1024 // 5MB in bytes
                    if (bytes <= maxFileSizeInBytes) {
                        // Display the selected image in the ImageView
                        selectedImageView.setImageURI(imageUri)
                        selectedImageView.visibility = ImageView.VISIBLE
                        // You can now work with the selected image (e.g., upload it)
                    } else {
                        // The selected image is too large
                        Toast.makeText(this, "Selected image is too large. Please choose a smaller image.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipepage1)

        recipeNameEditText = findViewById(R.id.recipeNameEditText)
        caloriesEditText = findViewById(R.id.caloriesEditText)
        ingredients=findViewById(R.id.editTextingredients)
        directions=findViewById(R.id.editTextdirections)
        selectedImageView = findViewById(R.id.selectedImageView)

        val imageUploadButton: Button = findViewById(R.id.imageUploadButton)
        imageUploadButton.setOnClickListener {
            openImageChooser()
        }

        val addButton: Button = findViewById(R.id.addButton)
        val k = admin_DBHelper(this)
        addButton.setOnClickListener {
            // Retrieve the values from the EditTexts
            val recipeName = recipeNameEditText.text.toString()
            val calories = caloriesEditText.text.toString()
            val ing= ingredients.text.toString()
            val dir=directions.text.toString()
            // Check if the recipe name and calories are not empty
            if (recipeName.isNotEmpty() && calories.isNotEmpty() && ing.isNotEmpty() && dir.isNotEmpty()) {
                // Convert the selected image to a ByteArray
                val imageByteArray = convertImageViewToByteArray(selectedImageView)

                // Insert the data into your database here
                k.insertrecipelist(imageByteArray, recipeName, calories,ing,dir)

                // Display a Toast message to confirm the addition
                val message = "Recipe added: $recipeName, Calories: $calories"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this,navigateto::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        imagePicker.launch(Intent.createChooser(intent, "Select Picture"))
    }
    // Function to convert ImageView to ByteArray
    private fun convertImageViewToByteArray(imageView: ImageView): ByteArray {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}