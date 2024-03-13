package com.example.intaketracker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CircularGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var maxCalories: Int = 0
    var consumedCalories: Int = 0
    private val strokeWidth: Float = 25f
    private val startAngle: Float = -90f

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CircularGraphView.strokeWidth
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 36f // Adjust text size as needed
        color = ContextCompat.getColor(context, R.color.black) // Text color
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Typeface for the text
    }

    private val bgRect = RectF()
    fun setMaxCaloriesFromDatabase(username: String, dbHelper: DBHelper): Int {
        // Fetch maxCalories from the database based on the username
        val calorieGoal = dbHelper.getCalorieGoal(username)

        // Set maxCalories to the retrieved value
        this.maxCalories = calorieGoal.toInt()
        invalidate()
        return this.maxCalories
    }

    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun setConsumedCaloriesFromDatabase(username: String, dbHelper: DBHelper): Int{
        val currentdate = Date()
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(currentdate)
        val consumedcal = dbHelper.getTotalCaloriesForDate(username, currentdate)
        Log.d("CircularGraphView", "Current Date: $formattedDate")
        Log.d("CircularGraphView", "Username: $username")
        Log.d("CircularGraphView", "Consumed Calories: $consumedcal")
        this.consumedCalories = consumedcal.toInt()
        invalidate()
        return this.consumedCalories
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sweepAngle = if (maxCalories != 0) {
            (consumedCalories / maxCalories.toFloat()) * 360f
        } else {
            0f
        }

        // Update the RectF's dimensions
        bgRect.set(
            strokeWidth / 2,
            strokeWidth / 2,
            width - strokeWidth / 2,
            height - strokeWidth / 2
        )
        // Fill the arc with green color based on consumed calories
        circlePaint.color = ContextCompat.getColor(context, R.color.green)
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, circlePaint)

        // Draw the remaining part of the arc with a different color
        circlePaint.color = ContextCompat.getColor(context, R.color.lightgreen)
        canvas.drawArc(bgRect, startAngle + sweepAngle, 360f - sweepAngle, false, circlePaint)

        // Draw consumed and total calories text
        val caloriesText = "$consumedCalories / $maxCalories"
        val textWidth = textPaint.measureText(caloriesText)
        val x = (width - textWidth) / 2
        val y = height / 2 - textPaint.textSize / 2 // Adjust the vertical position
        canvas.drawText(caloriesText, x, y, textPaint)
        val kcalText = "kcal"
        val kcalTextWidth = textPaint.measureText(kcalText)
        val kcalX = (width - kcalTextWidth) / 2
        val kcalY = height / 2 + textPaint.textSize // Adjust the vertical position

        canvas.drawText(kcalText, kcalX, kcalY, textPaint)
    }
}