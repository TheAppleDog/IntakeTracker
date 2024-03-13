package com.example.intaketracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, dbname, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val a =
            "CREATE TABLE usersign(username TEXT PRIMARY KEY, emailid TEXT, phoneno INTEGER CHECK(length(phoneno) = 10), pass TEXT CHECK(length(pass) = 8))"
        db.execSQL(a)
        val b = "CREATE TABLE googlesign(username TEXT PRIMARY KEY, emailid TEXT)"
        db.execSQL(b)
        val c =
            "CREATE TABLE userprofile(username TEXT, gender TEXT, age INTEGER, weight INTEGER, height INTEGER, kcal INTEGER, " +
                    "FOREIGN KEY(username) REFERENCES usersign(username), " +
                    "FOREIGN KEY(username) REFERENCES googlesign(username))"
        db.execSQL(c)
        val d = "CREATE TABLE userlogin(username TEXT, pass TEXT, " +
                "FOREIGN KEY(username) REFERENCES usersign(username))"
        db.execSQL(d)
        val e =
            "CREATE TABLE nutrition(nutritionID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, Date DATETIME DEFAULT CURRENT_TIMESTAMP, foodname TEXT, mealname TEXT,servingSize INTEGER, noOfServings INTEGER, Calories INTEGER, Carbs INTEGER, Protein INTEGER, Fat INTEGER," +
                    "FOREIGN KEY(username) REFERENCES usersign(username), " +
                    "FOREIGN KEY(username) REFERENCES googlesign(username))"
        db.execSQL(e)
        val f =
            "CREATE TABLE workout(workoutID INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,Date DATETIME DEFAULT CURRENT_TIMESTAMP, exercise_name TEXT, sets INTEGER, reps INTEGER, duration INTEGER, calories_burned DOUBLE,"+
                    "FOREIGN KEY(username) REFERENCES usersign(username), " +
                    "FOREIGN KEY(username) REFERENCES googlesign(username))"
        db.execSQL(f)
        val g = "CREATE TABLE waterintake(waterID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, Date DATETIME DEFAULT CURRENT_TIMESTAMP, amountML DOUBLE,"+
                "FOREIGN KEY(username) REFERENCES usersign(username), " +
                "FOREIGN KEY(username) REFERENCES googlesign(username))"
        db.execSQL(g)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usersign")
        db.execSQL("DROP TABLE IF EXISTS googlesign")
        db.execSQL("DROP TABLE IF EXISTS userprofile")
        db.execSQL("DROP TABLE IF EXISTS userlogin")
        db.execSQL("DROP TABLE IF EXISTS nutrition")
        db.execSQL("DROP TABLE IF EXISTS workout")
        db.execSQL("DROP TABLE IF EXISTS waterintake")
        onCreate(db)
    }
    fun getKcalByUsername(username: String): Int {
        val db = this.readableDatabase
        var kcal = -1 // Default value if kcal is not found
        // Query to fetch kcal based on username
        val query = "SELECT kcal FROM userprofile WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        // Check if the cursor has results and kcal column exists
        if (cursor.moveToFirst()) {
            val kcalIndex = cursor.getColumnIndex("kcal")

            // Check if kcal column exists in the result set
            if (kcalIndex != -1) {
                // Get kcal value from the cursor
                kcal = cursor.getInt(kcalIndex)

                // Ensure the kcal value is non-negative
                if (kcal < 0) {
                    kcal = 0
                }
            }
        }
        // Close the cursor and database
        cursor.close()
        db.close()

        // Return the non-negative kcal value
        return kcal
    }
    fun insertWaterIntake(username: String,amountML: Double) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("Date", getCurrentTimestamp())
        values.put("amountML", amountML)
        val result = db.insert("waterintake", null, values)
        if (result == -1L) {
            // Insertion failed, log an error or show a message.
            Log.e("DBHelper", "Failed to insert water intake")
        } else {
            Log.d("DBHelper", "Water intake inserted successfully")
        }
        db.close()
    }
    fun getWaterIntakeByDate(username: String, date: Date): Double {
        val db = this.readableDatabase
        var totalWaterIntake = 0.0 // Use Double instead of Int
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT SUM(amountML)
        FROM waterintake
        WHERE username = ? AND Date LIKE ?
    """.trimIndent()
        val selectionArgs = arrayOf(username, "$formattedDate%") // Match any time on that date

        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val columnIndex = cursor.getColumnIndex("SUM(amountML)")
                if (columnIndex >= 0) {
                    if (cursor.moveToFirst()) {
                        totalWaterIntake = cursor.getDouble(columnIndex) // Use getDouble
                    }
                } else {
                    // Handle the case where the column does not exist
                    // You can log an error or handle it as needed
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return totalWaterIntake
    }
    // Insert nutrition function
    fun insertNutritionData(username: String,foodName: String,mealname: String,servingSize: Int,noOfServings: Int,calories: Int,Carbs: Int,Protein: Int,Fat: Int) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("username", username)
            put("Date", getCurrentTimestamp())
            put("foodname", foodName)
            put("mealname", mealname)
            put("servingSize", servingSize)
            put("noOfServings", noOfServings)
            put("Calories", calories)
            put("Carbs", Carbs)
            put("Protein", Protein)
            put("Fat", Fat)
        }
        db.insert("nutrition", null, contentValues)
        db.close()
    }
    private fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
    fun getExerciseData(username: String, date: Date): List<String> {
        val db = this.readableDatabase
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val exerciseData = mutableListOf<String>()
        val query = """
        SELECT exercise_name,duration, calories_burned
        FROM workout
        WHERE username = ? AND Date = ?
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val exerciseNameIndex = cursor.getColumnIndex("exercise_name")
                val durationIndex=cursor.getColumnIndex("duration")
                val caloriesBurnedIndex = cursor.getColumnIndex("calories_burned")
                while (cursor.moveToNext()) {
                    val exerciseName = if (exerciseNameIndex != -1) cursor.getString(exerciseNameIndex) else ""
                    val duration=if(durationIndex !=-1) cursor.getInt(durationIndex) else ""
                    val caloriesBurned = if (caloriesBurnedIndex != -1) cursor.getInt(caloriesBurnedIndex) else 0
                    val exerciseInfo = "$exerciseName   $duration mins   $caloriesBurned kcal"
                    exerciseData.add(exerciseInfo)
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return exerciseData
    }
    fun getAggregateCaloriesBurnedForDate(username: String, date: Date): Double {
        val db = this.readableDatabase
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT SUM(calories_burned) AS total_calories
        FROM workout
        WHERE username = ? AND Date = ?
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        var totalCalories = 0.0
        if (cursor != null) {
            try {
                val columnIndex = cursor.getColumnIndex("total_calories")
                // Check if the column exists before retrieving its value
                if (columnIndex >= 0) {
                    if (cursor.moveToFirst()) {
                        totalCalories = cursor.getDouble(columnIndex)
                    }
                } else {
                    // Handle the case where the column doesn't exist
                    // You can log an error or handle it as needed
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return totalCalories
    }
    fun getBreakfastFoodData(username: String, date: Date): List<String> {
        val db = this.readableDatabase
        val foodData = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT foodname, Calories
        FROM nutrition
        WHERE username = ? AND Date = ? AND mealname = 'Breakfast'
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val foodnameIndex = cursor.getColumnIndex("foodname")
                val caloriesIndex = cursor.getColumnIndex("Calories")
                while (cursor.moveToNext()) {
                    val foodname = if (foodnameIndex != -1) cursor.getString(foodnameIndex) else ""
                    val calories = if (caloriesIndex != -1) cursor.getInt(caloriesIndex) else 0
                    val foodInfo = "$foodname $calories Kcal"
                    foodData.add(foodInfo)
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return foodData
    }
    fun getLunchFoodnames(username: String, date: Date): List<String> {
        val db = this.readableDatabase
        val foodnames = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT foodname, Calories
        FROM nutrition
        WHERE username = ? AND Date = ? AND mealname = 'Lunch'
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val foodnameIndex = cursor.getColumnIndex("foodname")
                val caloriesIndex = cursor.getColumnIndex("Calories")
                    while (cursor.moveToNext()) {
                        val foodname = if (foodnameIndex != -1) cursor.getString(foodnameIndex) else ""
                        val calories = if (caloriesIndex != -1) cursor.getInt(caloriesIndex) else 0
                        val foodInfo = "$foodname $calories Kcal"
                        foodnames.add(foodInfo)
                    }
                } finally {
                cursor.close()
            }
        }
        db.close()
        return foodnames
    }
    fun getSnacksFoodnames(username: String, date: Date): List<String> {
        val db = this.readableDatabase
        val foodnames = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT foodname, Calories
        FROM nutrition
        WHERE username = ? AND Date = ? AND mealname = 'Snacks'
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val foodnameIndex = cursor.getColumnIndex("foodname")
                val caloriesIndex = cursor.getColumnIndex("Calories")
                while (cursor.moveToNext()) {
                    val foodname = if (foodnameIndex != -1) cursor.getString(foodnameIndex) else ""
                    val calories = if (caloriesIndex != -1) cursor.getInt(caloriesIndex) else 0
                    val foodInfo = "$foodname $calories Kcal"
                    foodnames.add(foodInfo)
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return foodnames
    }
    fun getDinnerFoodnames(username: String, date: Date): List<String> {
        val db = this.readableDatabase
        val foodnames = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        val query = """
        SELECT foodname, Calories
        FROM nutrition
        WHERE username = ? AND Date = ? AND mealname = 'Dinner'
    """.trimIndent()
        val selectionArgs = arrayOf(username, formattedDate)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor != null) {
            try {
                val foodnameIndex = cursor.getColumnIndex("foodname")
                val caloriesIndex = cursor.getColumnIndex("Calories")
                while (cursor.moveToNext()) {
                    val foodname = if (foodnameIndex != -1) cursor.getString(foodnameIndex) else ""
                    val calories = if (caloriesIndex != -1) cursor.getInt(caloriesIndex) else 0
                    val foodInfo = "$foodname $calories Kcal"
                    foodnames.add(foodInfo)
                }
            } finally {
                cursor.close()
            }
        }
        db.close()
        return foodnames
    }
    fun getTotalCaloriesForDate(username: String, date: Date): Double {
        val db = this.readableDatabase
        var totalCalories = 0.0
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val query = """
            SELECT SUM(Calories) AS TotalCalories
            FROM nutrition
            WHERE username = ? AND Date = ?;
        """.trimIndent()
            val selectionArgs = arrayOf(username, formattedDate)
            val cursor = db.rawQuery(query, selectionArgs)

            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("TotalCalories")
                if (columnIndex >= 0) {
                    totalCalories = cursor.getDouble(columnIndex)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CircularGraphView", "Error querying database: ${e.message}")
            // Handle the exception here if needed
        } finally {
            db.close()
        }
        return totalCalories
    }
    fun getConsumedCaloriesData(username: String, date: Date): List<Double> {
        val db = this.readableDatabase
        val consumedCaloriesData = mutableListOf<Double>()
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val query = """
            SELECT Calories
            FROM nutrition
            WHERE username = ? AND Date = ?;
        """.trimIndent()
            val selectionArgs = arrayOf(username, formattedDate)
            val cursor = db.rawQuery(query, selectionArgs)
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex("Calories")
                if (columnIndex >= 0) {
                    val consumedCalories = cursor.getDouble(columnIndex)
                    consumedCaloriesData.add(consumedCalories)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CircularGraphView", "Error querying database: ${e.message}")
            // Handle the exception here if needed
        } finally {
            db.close()
        }

        return consumedCaloriesData
    }

    fun getAggregateCarbsForDate(username: String, date: Date): Int {
        val db = this.readableDatabase
        var totalCarbs = 0
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val query = """
            SELECT SUM(Carbs) AS TotalCarbs
            FROM nutrition
            WHERE username = ? AND Date = ?;
        """.trimIndent()
            val selectionArgs = arrayOf(username, formattedDate)
            val cursor = db.rawQuery(query, selectionArgs)
            if (cursor.moveToFirst()) {
                val totalCarbsIndex = cursor.getColumnIndex("TotalCarbs")

                if (totalCarbsIndex >= 0) {
                    totalCarbs = cursor.getInt(totalCarbsIndex)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CircularGraphView", "Error querying database for carbs: ${e.message}")
            // Handle the exception here if needed
        } finally {
            db.close()
        }
        return totalCarbs
    }
    fun getAggregateProteinForDate(username: String, date: Date): Int {
        val db = this.readableDatabase
        var totalProtein = 0
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val query = """
            SELECT SUM(Protein) AS TotalProtein
            FROM nutrition
            WHERE username = ? AND Date = ?;
        """.trimIndent()
            val selectionArgs = arrayOf(username, formattedDate)
            val cursor = db.rawQuery(query, selectionArgs)
            if (cursor.moveToFirst()) {
                val totalProteinIndex = cursor.getColumnIndex("TotalProtein")

                if (totalProteinIndex >= 0) {
                    totalProtein = cursor.getInt(totalProteinIndex)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CircularGraphView", "Error querying database for protein: ${e.message}")
            // Handle the exception here if needed
        } finally {
            db.close()
        }
        return totalProtein
    }
    fun getAggregateFatForDate(username: String, date: Date): Int {
        val db = this.readableDatabase
        var totalFat = 0
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)
            val query = """
            SELECT SUM(Fat) AS TotalFat
            FROM nutrition
            WHERE username = ? AND Date = ?;
        """.trimIndent()
            val selectionArgs = arrayOf(username, formattedDate)
            val cursor = db.rawQuery(query, selectionArgs)
            if (cursor.moveToFirst()) {
                val totalFatIndex = cursor.getColumnIndex("TotalFat")

                if (totalFatIndex >= 0) {
                    totalFat = cursor.getInt(totalFatIndex)
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CircularGraphView", "Error querying database for fat: ${e.message}")
            // Handle the exception here if needed
        } finally {
            db.close()
        }
        return totalFat
    }
    fun insertWorkout(
        username: String,
        exerciseName: String,
        sets: Int,
        reps: Int,
        duration: Int,
        caloriesBurned:Double
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("Date",getCurrentTimestamp())
        values.put("exercise_name", exerciseName)
        values.put("sets", sets)
        values.put("reps", reps)
        values.put("duration", duration)
        values.put("calories_burned", caloriesBurned)
        // Insert the data into the "workout" table
        db.insert("workout", null, values)
        // Close the database connection
        db.close()
    }

    // Insert user registration
    fun insertUser(username: String, email: String, phoneno: String, password: String): Long {
        val writableDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("emailid", email)
        // Check for whitespace in the username
        if (username.contains(" ")) {
            writableDatabase.close()
            return -1
        }
        // Email format validation using a regular expression
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            writableDatabase.close()
            return -1
        }
        if (phoneno.length == 10) {
            contentValues.put("phoneno", phoneno)
        } else {
            writableDatabase.close() // Close the database before returning
            return -1
        }
        if (password.length == 8) {
            contentValues.put("pass", password)
        } else {
            writableDatabase.close() // Close the database before returning
            return -1
        }
        val result = writableDatabase.insert("usersign", null, contentValues)
        writableDatabase.close() // Close the database after insertion
        return result
    }
    // Define a function to check if a username exists in the database
    fun isUsernameExist(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM usersign WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var usernameExists = false
        try {
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                usernameExists = count > 0
            }
        } finally {
            cursor.close()
        }
        db.close()
        return usernameExists
    }
    // Insert Google signup
    fun insertGoogleAccount(googleEmail: String) {
        var usernme: String?
        val database = writableDatabase
        usernme = googleEmail.substring(0, googleEmail.indexOf('@'))
        val google = ContentValues()
        google.put("username", usernme)
        google.put("emailid", googleEmail)
        database.insert("googlesign", null, google)
        database.close()
    }

    // Insert User Profile
    fun insertuserprofile(username: String, gender: String?, age: String, weight: String, height: String, kcal: String) {
        val profile = ContentValues()
        profile.put("username", username)
        profile.put("gender",gender )
        profile.put("age", age)
        profile.put("weight", weight)
        profile.put("height", height)
        profile.put("kcal",kcal)
        val database = writableDatabase
        database.insert("userprofile", null, profile)
        database.close()
    }
    // Add this function to your database helper class
    fun fetchWeightByUsername(username: String): Double {
        val database = readableDatabase
        val selection = "username = ?"
        val selectionArgs = arrayOf(username)
        val columns = arrayOf("weight")
        var weight = 0.0
        val cursor = database.query("userprofile", columns, selection, selectionArgs, null, null, null)
        if (cursor.moveToFirst()) {
            val weightColumnIndex = cursor.getColumnIndex("weight")
            if (weightColumnIndex != -1) {
                // Retrieve the weight from the cursor
                weight = cursor.getDouble(weightColumnIndex)
            }
        }
        cursor.close()
        database.close()
        return weight
    }
    fun getCalorieGoal(username: String): Double {
        val db = this.readableDatabase
        var calorieGoal = 0.0
        val query = "SELECT kcal FROM userprofile WHERE username = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.rawQuery(query, selectionArgs)
        if (cursor.moveToFirst()) {
            calorieGoal = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return calorieGoal
    }
    //Insert User Login
    fun insertlogin(username: String,password: String) {
        val database = writableDatabase
        // Check if this is a new device installation
        val cv = ContentValues()
        if (!checkUserCredentials(username,password)) {
            cv.put("username", username)
            cv.put("pass", password)
            database.insert("userlogin", null, cv)
        }
        database.close()
    }
       //check whether login data already exists
    fun checkUserCredentials(username: String, pass: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM userlogin WHERE username = ? AND pass = ?"
        val cursor = db.rawQuery(query, arrayOf(username, pass))
        val result = cursor.count > 0
        cursor.close()
        return result
    }
    //check login match with signup
    fun checkLogin(username: String, password: String): Boolean {
        val db = readableDatabase
        var hasMatchingLogin = false
        var cursor: Cursor? = null
        try {
            val selection = "username = ? AND pass = ?"
            val selectionArgs = arrayOf(username, password)
            cursor = db.query("usersign", null, selection, selectionArgs, null, null, null)
            if (cursor.moveToFirst()) {
                hasMatchingLogin = true
            }
        } catch (e: Exception) {
            // Handle exceptions here
        } finally {
            cursor?.close()
            db.close()
        }
        return hasMatchingLogin
    }

    companion object {
        private const val dbname = "intake-tracker.db"
    }
}