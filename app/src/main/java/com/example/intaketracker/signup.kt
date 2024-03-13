package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class signup : AppCompatActivity() {
    private lateinit var sessionManagement: session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val user = findViewById<EditText>(R.id.username)
        val emailid = findViewById<EditText>(R.id.emailid)
        val phoneno = findViewById<EditText>(R.id.phone)
        val password = findViewById<EditText>(R.id.password)
        val btn_signup = findViewById<Button>(R.id.sign_up)
        val eye = findViewById<ImageView>(R.id.eye1)
        val g = DBHelper(this)
        sessionManagement = session(this) // Initialize session management

        val lockedIcon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_24, null)
        val unlockedIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_open_24, null)

        password.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val drawable = if (hasFocus) unlockedIcon else lockedIcon
            password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }

        eye.setOnClickListener {
            togglePasswordVisibility()
        }
        btn_signup.setOnClickListener {
            val username = user.text.toString()
            val email = emailid.text.toString()
            val phone = phoneno.text.toString()
            val pass = password.text.toString()
            var isValid = true
            if (username.isBlank() || email.isBlank() || phone.isBlank() || pass.isBlank()) {
                Toast.makeText(
                    applicationContext, "ENTER ALL DETAILS.",
                    Toast.LENGTH_SHORT
                ).show()
                isValid = false
            }
            if (phone.length != 10) {
                phoneno.error = "Phone number must be 10 digits"
                isValid = false
            }
            if (pass.length != 8) {
                password.error = "Password must be 8 characters"
                isValid = false
            }
            // Check if the username contains spaces
            if (username.contains(" ")) {
                user.error = "Username cannot contain spaces"
                isValid = false
            }
            // Email format validation using a regular expression
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]+"
            if (!email.matches(emailPattern.toRegex())) {
                emailid.error = "Invalid email format"
                isValid = false
            }
            if (isValid) {
                // Validation successful, proceed with signup
                // Check if the username already exists in the database
                if (g.isUsernameExist(username)) {
                    user.error = "Username already exists. Please choose another username."
                } else {
                    try {
                        g.insertUser(username, email, phone, pass)
                        sessionManagement.saveUsername(username)
                        Toast.makeText(
                            applicationContext, "Authentication Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(application, userinfo::class.java)
                        intent.putExtra("message_key", username)
                        startActivity(intent)
                        finish()
                    } catch (_: Exception) {
                        // Handle exceptions if needed
                    }
                }
            }
        }
    }

        private fun togglePasswordVisibility() {
        val pass = findViewById<EditText>(R.id.password)
        val eye = findViewById<ImageView>(R.id.eye1)
        if (pass.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // Show Password
            pass.transformationMethod = null
            eye.setImageResource(R.drawable.baseline_visibility_24) // Change to open eye icon
        } else {
            // Hide Password
            pass.transformationMethod = PasswordTransformationMethod.getInstance()
            eye.setImageResource(R.drawable.baseline_visibility_off_24) // Change to closed eye icon
        }
        pass.setSelection(pass.text.length)
    }
}