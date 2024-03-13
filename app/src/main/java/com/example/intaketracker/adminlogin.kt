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

class adminlogin : AppCompatActivity() {
    private lateinit var sessionManagement: session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminlogin)
        val username = findViewById<EditText>(R.id.usern)
        val pass = findViewById<EditText>(R.id.pass)
        val btn_login = findViewById<Button>(R.id.Login)
        sessionManagement = session(this) // Initialize session management
        btn_login.setOnClickListener {
            if (username.text.toString() == "ADMIN"||pass.text.toString() == "appledog2002") {
                val P = Intent(application, navigateto::class.java)
                startActivity(P)
                finish()
            } else {
                Toast.makeText(applicationContext, "NO USER FOUND OR RE-CHECK PASSWORD ", Toast.LENGTH_LONG).show()
            }
        }
        val eye = findViewById<ImageView>(R.id.eye1)
        val lockedIcon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_24, null)
        val unlockedIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_open_24, null)

        pass.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val drawable = if (hasFocus) unlockedIcon else lockedIcon
            pass.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
        eye.setOnClickListener {
            togglePasswordVisibility()
        }
    }
        private fun togglePasswordVisibility() {
            val pass = findViewById<EditText>(R.id.pass)
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
