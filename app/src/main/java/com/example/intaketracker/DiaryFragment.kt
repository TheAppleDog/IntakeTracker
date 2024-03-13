package com.example.intaketracker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiaryFragment : Fragment() {
    private var dateTextView: TextView? = null
    private val calendar = Calendar.getInstance()
    private var gl: TextView?=null
    private var remain: TextView?=null
    private var intake:TextView?=null
    private var burned:TextView?=null
    private lateinit var BF: TextView
    private lateinit var l:TextView
    private lateinit var SN:TextView
    private lateinit var Dine:TextView
    private lateinit var water: TextView
    private lateinit var exercise:TextView
    private lateinit var k: DBHelper
    private lateinit var sessionManagement: session
    private lateinit var breakfast:CardView
    private lateinit var a:ImageView
    private lateinit var editButton: ImageView
    private var maxCalories: Int = 0
    private var consumedCalories: Int=0
    private val MAX_CALORIES_PREF_KEY = "max_calories"

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_diary, container, false)
         gl=v.findViewById<TextView>(R.id.goal)
        remain=v.findViewById<TextView>(R.id.remaining)
        intake=v.findViewById<TextView>(R.id.food)
        burned=v.findViewById(R.id.exercise)
        dateTextView = v.findViewById(R.id.dateTextView)
        breakfast=v.findViewById(R.id.breakfast)
        BF=v.findViewById(R.id.addbf)
        l=v.findViewById(R.id.addlunch)
        SN=v.findViewById(R.id.addsnacks)
        Dine=v.findViewById(R.id.adddine)
        water=v.findViewById(R.id.addwater)
        exercise=v.findViewById(R.id.addex)
// Initialize maxCalories from SharedPreferences
//        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        maxCalories = sharedPreferences.getInt(MAX_CALORIES_PREF_KEY, 0)
//
// val circularGraphView = requireActivity().findViewById<CircularGraphView>(R.id.caloriesGraph)
//        if (circularGraphView != null) {
//            maxCalories = circularGraphView.maxCalories
//        } else {
//            // Handle the case where circularGraphView is null
//        }
        sessionManagement = session(requireContext()) // Initialize session management
        v.findViewById<View>(R.id.leftAngular).setOnClickListener {
            // Go to the previous day
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            updateDateTextView()
        }

        v.findViewById<View>(R.id.rightAngular).setOnClickListener {
            // Go to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            updateDateTextView()
        }

        // Set a click listener for the dateTextView to show a DatePicker
        dateTextView?.setOnClickListener {
            showDatePickerDialog()
        }

        return v
    }
    private fun calculateRemainingCalories(goal: Int, intake: Int, burned: Int): Int {
        return goal - intake + burned
    }
    override fun onResume() {
        super.onResume()

        val username = sessionManagement.getUsername()
        username.toString()
        val usr=username.toString()
        val g = DBHelper(requireContext())
        // Fetch maxCalories from SharedPreferences if not already set
//        if (maxCalories == 0) {
//            val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            maxCalories = sharedPreferences.getInt(MAX_CALORIES_PREF_KEY, 0)
//        }
        // Update UI with maxCalories
       // gl?.text = maxCalories.toString()
        gl?.text= g.getKcalByUsername(usr).toString()
        val currentdate = Date()
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentdate)
        Log.d("DiaryFragment", "Current Date: $formattedDate")
        Log.d("DiaryFragment", "Username: $username")
        k= DBHelper(requireContext())
        updateDateTextView()
    }
     private fun updateDateTextView() {
        val currentDate = calendar.time
        val today = Calendar.getInstance()

        // Clear time fields to compare dates only
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val currentDateWithoutTime = calendar.clone() as Calendar
        currentDateWithoutTime.set(Calendar.HOUR_OF_DAY, 0)
        currentDateWithoutTime.set(Calendar.MINUTE, 0)
        currentDateWithoutTime.set(Calendar.SECOND, 0)
        currentDateWithoutTime.set(Calendar.MILLISECOND, 0)

        val diffMillis = currentDateWithoutTime.timeInMillis - today.timeInMillis
        val daysDifference = (diffMillis / (24 * 60 * 60 * 1000)).toInt()

        when (daysDifference) {
            0 -> dateTextView?.text = getString(R.string.today)
            -1 -> dateTextView?.text = getString(R.string.yesterday)
            1 -> dateTextView?.text = getString(R.string.tomorrow)
            else -> {
                val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
                dateTextView?.text = dateFormat.format(currentDate)
            }
        }
        val username = sessionManagement.getUsername()
        val usr=username.toString()
        // Fetch and display food names for the selected date
        val breakfastFoodnames = k.getBreakfastFoodData(usr, currentDate)
        val breakfastFoodnamesString = breakfastFoodnames.joinToString("\n")
        BF.text = breakfastFoodnamesString
        val lunchFoodnames = k.getLunchFoodnames(usr, currentDate)
        val lunchFoodnamesString = lunchFoodnames.joinToString("\n")
        l.text = lunchFoodnamesString
        val snacksFoodnames = k.getSnacksFoodnames(usr, currentDate)
        val snacksFoodnamesString = snacksFoodnames.joinToString("\n")
        SN.text = snacksFoodnamesString
        val dinnerFoodnames = k.getDinnerFoodnames(usr, currentDate)
        val dinnerFoodnamesString = dinnerFoodnames.joinToString("\n")
        Dine.text = dinnerFoodnamesString
        val aggregatedAmountML = k.getWaterIntakeByDate(usr, currentDate)
        Log.d("WaterIntake", "Fetched value: $aggregatedAmountML")
        if (aggregatedAmountML == 0.0) {
            water.text = ""
        } else {
            val formattedAmount = String.format("%.2f litres", aggregatedAmountML)
            water.text = formattedAmount
        }
        val exercisenames=k.getExerciseData(usr,currentDate)
        val exercisenameString=exercisenames.joinToString("\n")
        exercise.text=exercisenameString
         burned?.text = k.getAggregateCaloriesBurnedForDate(usr, currentDate).toInt().toString()

         val consumedCaloriesDouble = k.getTotalCaloriesForDate(usr, currentDate)
         val consumedCaloriesInt = consumedCaloriesDouble.toInt()
         intake?.text = consumedCaloriesInt.toString()
         //gl?.text = maxCalories.toString()
         val goal = k.getKcalByUsername(usr)
         gl?.text= goal.toString()
         val caloriesBurned=burned?.text.toString().toInt()
         val remaining = calculateRemainingCalories(goal, consumedCaloriesInt, caloriesBurned)

         // Update the remaining TextView
         remain?.text = remaining.toString()
     }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                updateDateTextView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }
}
