package com.example.intaketracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {
    private lateinit var circularGraphView: CircularGraphView
    private lateinit var session:session
    private lateinit var t1: TextView
    private lateinit var t2: TextView
    private lateinit var t3: TextView
    private lateinit var t4: TextView
    private lateinit var t5: TextView
    private lateinit var t6: TextView
    private lateinit var carbsProgressBar: ProgressBar
    private lateinit var proteinProgressBar: ProgressBar
    private lateinit var fatProgressBar: ProgressBar
    private lateinit var water: CardView
    private lateinit var exercisecardview: CardView
    private lateinit var graph: LineChart
    //private lateinit var notif: ImageView
    private lateinit var usr: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dashboard, container, false)
        circularGraphView = v.findViewById(R.id.caloriesGraph)
        val username = arguments?.getString("USERNAME_EXTRA_KEY")
       usr = v.findViewById<TextView>(R.id.usr)
        val img1 = v.findViewById<ImageView>(R.id.bf)
        val img2 = v.findViewById<ImageView>(R.id.lunch)
        val img3 = v.findViewById<ImageView>(R.id.snacks)
        val img4 = v.findViewById<ImageView>(R.id.dine)
        //notif = v.findViewById(R.id.notifs)
        graph = v.findViewById(R.id.graph)
        session= session(requireContext())
//        notif.setOnClickListener {
//            val intent = Intent(requireContext(), notifs::class.java)
//            startActivity(intent)
//        }
        carbsProgressBar = v.findViewById(R.id.carbsProgressBar)
        proteinProgressBar = v.findViewById(R.id.proteinProgressBar)
        fatProgressBar = v.findViewById(R.id.fatProgressBar)
        water = v.findViewById(R.id.waterCardView)
        exercisecardview = v.findViewById<CardView>(R.id.exerciseCardView)
        t1 = v.findViewById(R.id.t1)
        t2 = v.findViewById(R.id.t2)
        t3 = v.findViewById(R.id.t3)
        t4 = v.findViewById(R.id.t4)
        t5 = v.findViewById(R.id.t5)
        t6 = v.findViewById(R.id.t6)
        img1.setOnClickListener {
            val intent = Intent(activity, foodlist::class.java)
            startActivity(intent)
        }
        img2.setOnClickListener {
            val intent = Intent(activity, foodlist::class.java)
            startActivity(intent)
        }
        img3.setOnClickListener {
            val intent = Intent(activity, foodlist::class.java)
            startActivity(intent)
        }
        img4.setOnClickListener {
            val intent = Intent(activity, foodlist::class.java)
            startActivity(intent)
        }
        water.setOnClickListener {
            val intent = Intent(activity, water1::class.java)
            startActivity(intent)
        }
        exercisecardview.setOnClickListener {
            val intent = Intent(activity, exerciselist::class.java)
            startActivity(intent)
        }
        val diaryFragment = DiaryFragment()
        val args = Bundle()
        diaryFragment.arguments = args
        return v
    }
    override fun onResume() {
        super.onResume()// Restore or reinitialize data here
        val username = arguments?.getString("USERNAME_EXTRA_KEY")
        usr.text=username
        val u=usr.text.toString()
        val dbHelper = DBHelper(requireContext())
        // Example: Reinitialize data
        if (username != null) {
            circularGraphView.setMaxCaloriesFromDatabase(u, dbHelper)
            circularGraphView.setConsumedCaloriesFromDatabase(u, dbHelper)
        }
        usr.text = username
        updateGraph()
        // Add the refresh code here
        val g = DBHelper(requireContext())
        val currentdate = Date()
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentdate)
        Log.d("DashboardFragment", "Current Date: $formattedDate")
        Log.d("DashboardFragment", "Username: $u")
        val carbs = g.getAggregateCarbsForDate(u, currentdate)
        val proteins = g.getAggregateProteinForDate(u, currentdate)
        val fats = g.getAggregateFatForDate(u, currentdate)
        t1.text = carbs.toString()
        t3.text = proteins.toString()
        t5.text = fats.toString()
        // Calculate t2, t4, and t6 based on maxCalories
        val maxCalories = circularGraphView.maxCalories
        val t2Value = ((maxCalories * 54.96) / 100).toInt()
        val t4Value = ((maxCalories * 15.01) / 100).toInt()
        val t6Value = ((maxCalories * 30.02) / 100).toInt()

        t2.text = t2Value.toString()
        t4.text = t4Value.toString()
        t6.text = t6Value.toString()
        // Set progress values for carbs, protein, and fat
        carbsProgressBar.max = t2Value
        proteinProgressBar.max = t4Value
        fatProgressBar.max = t6Value
// Set the minimum values of progress bars to t1, t3, and t5
        carbsProgressBar.progress = t1.text.toString().toInt()
        proteinProgressBar.progress = t3.text.toString().toInt()
        fatProgressBar.progress = t5.text.toString().toInt()
    }

    private fun updateGraph() {
        // Get consumed calories data for the current day
        val currentdate = Date()
        val g = DBHelper(requireContext())
        val username = arguments?.getString("USERNAME_EXTRA_KEY")
        usr.text=username
        val u=usr.text.toString()
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentdate)
// Call getConsumedCaloriesData with the date range
        val consumedCaloriesData = g.getConsumedCaloriesData(u,currentdate)

        // Customize X-axis
        val xAxis: XAxis = graph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // 1 unit between labels (1 hour)
        xAxis.isGranularityEnabled = true // Enable granularity
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = 23f // Display labels for the current day (24 hours)

        val dateLabels = mutableListOf<String>()
// Calculate the labels for each hour within the current day
        for (hour in 0..23) { // Loop through 24 hours
            val calendar = Calendar.getInstance()
            calendar.time = currentdate
            calendar.set(Calendar.HOUR_OF_DAY, hour)

            val label = SimpleDateFormat("dd/MM", Locale.getDefault()).format(calendar.time)
            dateLabels.add(label)
        }
        xAxis.valueFormatter = object : IndexAxisValueFormatter(dateLabels) {
            override fun getFormattedValue(value: Float): String {
                // Ensure that the value is within the bounds of your date labels
                val index = value.toInt()
                return if (index >= 0 && index < dateLabels.size) {
                    dateLabels[index]
                } else {
                    "" // Return an empty string for out-of-range values
                }
            }
        }
// Set the number of visible labels to 1
        xAxis.labelCount = 1
        // Calculate the maximum value for the Y-axis with a gap of 500
        val maxY = calculateMaxCalories()
// Customize Y-axis
        val yAxis: YAxis = graph.axisLeft
        yAxis.axisMinimum = 0F // Minimum value
        yAxis.axisMaximum = maxY // Maximum value
// Determine the label count based on the Y-axis range
        val labelCount = (maxY / 500).toInt() + 1 // Adjust the 400 based on your gap
        yAxis.setLabelCount(labelCount, true)
        yAxis.setDrawLabels(true)
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }
        // Create a LineDataSet and add data points
        val entries = mutableListOf<Entry>()
        for (hour in consumedCaloriesData.indices) {
            val consumedCalories = consumedCaloriesData[hour].toFloat()
            entries.add(Entry(hour.toFloat(), consumedCalories))
        }
        val dataSet = LineDataSet(entries, "Consumed Calories")
        dataSet.color = Color.GREEN
        dataSet.setCircleColor(Color.GREEN)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        // Create a LineData object with the dataset(s)
        val lineData = LineData(dataSets)
        // Set data to the LineChart
        graph.data = lineData
        graph.scrollX
        // Refresh the chart
        graph.invalidate()
    }
    private fun calculateMaxCalories(): Float {
        // Get the maximum calories value (max calories) from your source
        val maxCalories = circularGraphView.maxCalories
        // Calculate the maximum value for the Y-axis with a gap of 400
        return (maxCalories / 500).toFloat() * 500
    }
}