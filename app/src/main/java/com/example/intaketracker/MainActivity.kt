package com.example.intaketracker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 5000 // 3 seconds
    private lateinit var sessionManagement: session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManagement = session(this) // Initialize session management
        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        sessionManagement. clearSession()
        // Animate the logo
        animateLogo(logoImageView)

//        fun startForegroundService() {
//            val serviceIntent = Intent(this, NotificationForegroundService::class.java)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(serviceIntent)
//            } else {
//                startService(serviceIntent)
//            }
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            val username = sessionManagement.getUsername()

            if ((username != null && sessionManagement.isLoggedIn()) || sessionManagement.getAuthToken() != null) {
                val intent = Intent(this, dashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                // Check if Google sign-up session exists
                val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
                if (googleSignInAccount != null) {
                    // User previously signed up with Google
                    // Navigate to dashboard
                    val googleUsername = googleSignInAccount.displayName ?: ""
                    sessionManagement.saveUsername(googleUsername)
                    val intent = Intent(this, dashboard::class.java)
                    intent.putExtra("message_key", googleUsername)
                    startActivity(intent)
                } else {
                    // User is not logged in, navigate to the login activity
                    val intent = Intent(this, login::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }, SPLASH_DELAY) // Delay in milliseconds
    }

    private fun animateLogo(logoImageView: ImageView?) {
        val textAnimationTextView = findViewById<TextView>(R.id.textAnimation)
        val initialScaleX = 0.5f
        val initialScaleY = 0.5f
        val targetScaleX = 1.5f
        val targetScaleY = 1.5f


        val zoomIn = AnimatorSet()
        zoomIn.play(ObjectAnimator.ofFloat(logoImageView, "scaleX", initialScaleX, targetScaleX))
            .with(ObjectAnimator.ofFloat(logoImageView, "scaleY", initialScaleY, targetScaleY))
        zoomIn.duration = 2700

        val zoomOut = AnimatorSet()
        zoomOut.play(ObjectAnimator.ofFloat(logoImageView, "scaleX", targetScaleX, initialScaleX))
            .with(ObjectAnimator.ofFloat(logoImageView, "scaleY", targetScaleY, initialScaleY))
        zoomOut.startDelay = 2700 // Start after zoomIn animation
        zoomOut.duration = 2700

        val alphabetText = "Intake Tracker" // The text you want to animate

        val handler = Handler(Looper.getMainLooper())
        var index = 0

        val textAnimationRunnable = object : Runnable {
            override fun run() {
                if (index < alphabetText.length) {
                    textAnimationTextView.text = alphabetText.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 100) // Adjust the delay as needed
                } else {
                    // Start the zoom animations
                    zoomIn.start()
                    zoomIn.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            zoomOut.start()
                        }
                    })
                }
            }
        }

        handler.post(textAnimationRunnable)
    }
}
