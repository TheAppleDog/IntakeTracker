package com.example.intaketracker

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlin.system.exitProcess

class dashboard : AppCompatActivity() {
    private var tablayout: TabLayout? = null
    private lateinit var sessionManagement: session
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var diaryFragment: DiaryFragment
    private lateinit var moreFragment: MoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sessionManagement = session(this) // Initialize session management
        tablayout = findViewById(R.id.tablayout)

        // Initialize fragments
        dashboardFragment = DashboardFragment()
        diaryFragment = DiaryFragment()
        moreFragment = MoreFragment()
        val username = intent.getStringExtra("message_key")

        val bundle = Bundle()
        bundle.putString("USERNAME_EXTRA_KEY", username)
        dashboardFragment.arguments = bundle
        // Load the default fragment (DashboardFragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, dashboardFragment)
            .commit()

        // Set the color state list for tab text colors
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(android.R.attr.state_enabled)
        )
        val colors = intArrayOf(
            Color.BLACK,  // Color when tab is selected
            Color.GRAY // Color when tab is not selected
        )
        val colorStateList = ColorStateList(states, colors)
        tablayout?.setTabIconTint(colorStateList)

        // Set tab icon colors
        tablayout?.setSelectedTabIndicatorColor(Color.BLACK)

        // Add tabs with icons
        tablayout?.let { safeTabLayout ->
            val first: TabLayout.Tab = safeTabLayout.newTab()
            first.text = "Dashboard"
            first.icon = ContextCompat.getDrawable(this, R.drawable.outline_home_24)
            safeTabLayout.addTab(first)

            val second: TabLayout.Tab = safeTabLayout.newTab()
            second.text = "Diary"
            second.icon = ContextCompat.getDrawable(this, R.drawable.baseline_menu_book_24)
            safeTabLayout.addTab(second)

            val third: TabLayout.Tab = safeTabLayout.newTab()
            third.text = "More"
            third.icon = ContextCompat.getDrawable(this, R.drawable.baseline_menu_24)
            safeTabLayout.addTab(third)
        }

        tablayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> replaceFragment(dashboardFragment)// Only pass username to DashboardFragment
                    1 -> replaceFragment(diaryFragment) // Don't pass username to DiaryFragment
                    2 -> replaceFragment(moreFragment) // Don't pass username to MoreFragment
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // No action needed
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // No action needed
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Close the app when the back button is pressed in this activity
        moveTaskToBack(true)
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(1)
    }
    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
