package com.example.intaketracker

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class login : AppCompatActivity() {
    private lateinit var googlebtn: ImageView
    private lateinit var googleSignInAccount: GoogleSignInAccount
    private lateinit var sessionManagement: session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)
        val btn_login = findViewById<Button>(R.id.login)
        val btn_admin = findViewById<Button>(R.id.adminlogin)
        val txt = findViewById<TextView>(R.id.txt)
        val signup = findViewById<TextView>(R.id.sign_up)
        val g = DBHelper(this)
        sessionManagement = session(this) // Initialize session management
        val sessionManager = session(applicationContext)

// After successful Google Sign-In
        val googleIdToken = "your_google_id_token"
        sessionManager.saveAuthToken(googleIdToken)

        val lockedIcon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_24, null)
        val unlockedIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.baseline_lock_open_24, null)

        pass.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val drawable = if (hasFocus) unlockedIcon else lockedIcon
            pass.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }

        signup.setOnClickListener {
            val i = Intent(this,com.example.intaketracker.signup::class.java)
            startActivity(i)
        }
        btn_admin.setOnClickListener{
            val o = Intent(this,adminlogin::class.java)
            startActivity(o)
        }
        val mstring = "Sign Up"
        val mSpannableString = SpannableString(mstring)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        txt.text = mSpannableString

        val eye = findViewById<ImageView>(R.id.eye1)
        eye.setOnClickListener {
            togglePasswordVisibility()
        }

        btn_login.setOnClickListener {
            val usern = username.text.toString()
            val passw = pass.text.toString()
            if (usern == "" || passw == "") {
                Toast.makeText(applicationContext, "ENTER DETAILS.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    g.insertlogin(usern, passw)
                    sessionManagement.saveUsername(usern)
                    sessionManagement.saveLoginStatus(true)
                    if (g.checkLogin(usern, passw)) {
                    if (g.checkUserCredentials(usern, passw)) {
                        Toast.makeText(applicationContext, "LogIn Successful", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(application, dashboard::class.java)
                        intent.putExtra("message_key", usern) // Pass the username
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle case when login was successful according to checkLogin(),
                        // but user credentials check (checkUserCredentials()) failed
                        Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                    }else {
                        Toast.makeText(applicationContext, "NO USER FOUND\nor Re-Check Password ", Toast.LENGTH_SHORT).show()
                    }
                } catch (_: Exception) {
                    // Handle exception
                }
            }
        }
        googlebtn = findViewById<ImageView>(R.id.google)
        val googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data: Intent? = result.data
                handleSignInResult(data)
            }
        txt.setOnClickListener {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                    .build()
            val gsc = GoogleSignIn.getClient(this, gso)

            val signInIntent = gsc.getSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            task.getResult(ApiException::class.java)
            navigatetosecondactivity()
            // Signed in successfully, handle the account data (e.g., display user info)
            // You can access the account information from the `account` object.
        } catch (e: ApiException) {
            // Sign in failed, handle the error
        }
    }

    private fun navigatetosecondactivity() {
        val g = DBHelper(this)
        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signInAccount != null) {
            googleSignInAccount = signInAccount
        } else {
            Toast.makeText(
                applicationContext, "Google SignUp Successful",
                Toast.LENGTH_SHORT
            ).show()  // Handle the case where user is not signed in
                }
        val googleUsername = googleSignInAccount.displayName ?: ""
        val googleEmail = googleSignInAccount.email ?: ""

        try {
            g.insertGoogleAccount(googleEmail)
            sessionManagement.saveUsername(googleUsername)
            sessionManagement.saveLoginStatus(true)
            Toast.makeText(
                applicationContext, "Google SignUp Successful",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(application, userinfo::class.java)
            intent.putExtra("message_key", googleUsername)
            startActivity(intent)
            finish()
        } catch (_: Exception) {

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