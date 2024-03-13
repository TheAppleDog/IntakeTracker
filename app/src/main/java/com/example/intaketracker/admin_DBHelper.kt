package com.example.intaketracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class admin_DBHelper(context: Context) : SQLiteOpenHelper(context, dbname, null, 1) {
    companion object {
        private const val dbname = "admin.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val a =
            "CREATE TABLE foodlist( food_item_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,serving_size TEXT, calories INTEGER)"
        db.execSQL(a)
        val b =
            "CREATE TABLE exerciselist(ex_id INTEGER PRIMARY KEY AUTOINCREMENT, ex_name TEXT, MET_Value DOUBLE)"
        db.execSQL(b)
        val c =
            "CREATE TABLE recipes(recipe_id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_image BLOB, recipe_name TEXT, calories TEXT,ingredients TEXT,directions TEXT)"
        db.execSQL(c)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }
    // Insert FOOD ITEM
    fun insertfoodlist(name: String, servingSize: String, calories: Int) {
        val food = ContentValues()
        val database = writableDatabase
        // Check if the food item already exists in the database
        val cursor = database.rawQuery("SELECT name FROM foodlist WHERE name=?", arrayOf(name))
        if (cursor.count == 0) {
            // The food item doesn't exist, so insert it
            food.put("name", name)
            food.put("serving_size", servingSize)
            food.put("calories", calories)
            database.insert("foodlist", null, food)
        }
        cursor.close()
        database.close()
    }
    fun insertexerciselist(ex_name: String,MET_Value: Double) {
        val ex = ContentValues()
        val database = writableDatabase
        val cursor = database.rawQuery("SELECT ex_name FROM exerciselist WHERE ex_name=?", arrayOf(ex_name))
        if (cursor.count == 0) {
            ex.put("ex_name", ex_name)
            ex.put("MET_Value", MET_Value)
            database.insert("exerciselist", null, ex)
        }
        cursor.close()
        database.close()
    }
    fun insertrecipelist(recipe_image:ByteArray, recipe_name: String, calories: String,ingredients:String,directions:String) {
        val recipe= ContentValues()
        val database = writableDatabase
        val cursor = database.rawQuery("SELECT recipe_name FROM recipes WHERE recipe_name=?", arrayOf(recipe_name))
        if (cursor.count == 0) {
            recipe.put("recipe_image", recipe_image)
            recipe.put("recipe_name", recipe_name)
            recipe.put("calories", calories)
            recipe.put("ingredients",ingredients)
            recipe.put("directions",directions)
            database.insert("recipes", null, recipe)
        }
        cursor.close()
        database.close()
    }
    fun getRecipeDetails(recipeName: String): Pair<List<String>, List<String>> {
        val database = readableDatabase
        val ingredients = mutableListOf<String>()
        val directions = mutableListOf<String>()
        val cursor = database.rawQuery("SELECT ingredients, directions FROM recipes WHERE recipe_name=?", arrayOf(recipeName))
        if (cursor.moveToFirst()) {
            val ingredientsIndex = cursor.getColumnIndex("ingredients")
            val directionsIndex = cursor.getColumnIndex("directions")
            if (ingredientsIndex != -1 && directionsIndex != -1) {
                val ingredientsData = cursor.getString(ingredientsIndex)
                val directionsData = cursor.getString(directionsIndex)
                // Split the comma-separated ingredients and directions into lists
                ingredients.addAll(ingredientsData.split(","))
                directions.addAll(directionsData.split(","))
            } else {
                // Handle the case where columns are not found
                // You can log an error or perform error handling here
            }
        }
        cursor.close()
        database.close()
        return ingredients to directions
    }
    // Fetch function for the 'name' column from the 'foodlist' table
    fun getFoodListNames(): List<String> {
        val foodListNames = mutableListOf<String>()
        val query = "SELECT name FROM foodlist"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndex("name")
            val value = cursor.getString(columnIndex)
            foodListNames.add(value)
        }
        cursor.close()
        db.close()
        return foodListNames
    }
    fun deleteFoodByName(foodName: String) {
        val db=writableDatabase
        val deleteQuery = "DELETE FROM foodlist WHERE name = ?"
        db.execSQL(deleteQuery, arrayOf(foodName))
    }
    fun deleteRecipeByName(recipeName: String) {
        val database = writableDatabase
        database.delete("recipes", "recipe_name = ?", arrayOf(recipeName))
        database.close()
    }
    fun deleteExerciseByName(exName: String) {
        val db=writableDatabase
        val deleteQuery = "DELETE FROM exerciselist WHERE ex_name = ?"
        db.execSQL(deleteQuery, arrayOf(exName))
    }

    fun updateCalories(foodname: String, newCalories: Int): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("calories", newCalories)
        val whereClause = "name = ?"
        val whereArgs = arrayOf(foodname)
        val rowsAffected = db.update("foodlist", contentValues, whereClause, whereArgs)
        db.close()
        return rowsAffected
    }
    fun updateMet(exname: String, newMet: Double?): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("MET_Value", newMet)
        val whereClause = "ex_name = ?"
        val whereArgs = arrayOf(exname)
        val rowsAffected = db.update("exerciselist", contentValues, whereClause, whereArgs)
        db.close()
        return rowsAffected
    }
    fun getCaloriesForFood(foodName: String): Int {
        val db = this.readableDatabase
        var calories = 0
        val query = "SELECT calories FROM foodlist WHERE name = ?"
        val cursor = db.rawQuery(query, arrayOf(foodName))
        try {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("calories")
                if (columnIndex != -1) {
                    calories = cursor.getInt(columnIndex)
                } else {
                    // Handle the case where the column index is -1
                    Log.e("admin_DBHelper", "Column index is -1")
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        return calories
    }
    fun getMetForExercise(exName: String): Double {
        val db = this.readableDatabase
        var MET = 0.0
        val query = "SELECT MET_Value FROM exerciselist WHERE ex_name = ?"
        val cursor = db.rawQuery(query, arrayOf(exName))
        try {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("MET_Value")
                if (columnIndex != -1) {
                    MET = cursor.getDouble(columnIndex)
                } else {
                    // Handle the case where the column index is -1
                    Log.e("admin_DBHelper", "Column index is -1")
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        return MET
    }
    fun getAllFood(): List<String> {
        val foodNames = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT name FROM foodlist"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex("name")
            if (nameColumnIndex != -1) {
                do {
                    val foodName = cursor.getString(nameColumnIndex)
                    foodNames.add(foodName)
                } while (cursor.moveToNext())
            } else {
                // Handle the case where the "name" column is not found
                // You can log an error, throw an exception, or handle it as needed.
            }
        }
        cursor.close()
        db.close()
        return foodNames
    }
    fun getAllRecipes(): List<String> {
        val recipeNames = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT recipe_name FROM recipes"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex("recipe_name")
            if (nameColumnIndex != -1) {
                do {
                    val foodName = cursor.getString(nameColumnIndex)
                    recipeNames.add(foodName)
                } while (cursor.moveToNext())
            } else {
                // Handle the case where the "name" column is not found
                // You can log an error, throw an exception, or handle it as needed.
            }
        }
        cursor.close()
        db.close()
        return recipeNames
    }
    fun getAllExercise(): List<String> {
        val exNames = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT ex_name FROM exerciselist"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val nameColumnIndex = cursor.getColumnIndex("ex_name")
            if (nameColumnIndex != -1) {
                do {
                    val exName = cursor.getString(nameColumnIndex)
                    exNames.add(exName)
                } while (cursor.moveToNext())
            } else {
                // Handle the case where the "name" column is not found
                // You can log an error, throw an exception, or handle it as needed.
            }
        }
        cursor.close()
        db.close()
        return exNames
    }


    data class Recipe(val recipeImage: ByteArray, val recipeName: String, val calories: String)

    fun fetchRecipes(): List<Recipe> {
        val recipeList = ArrayList<Recipe>()
        val db = this.readableDatabase
        val query = "SELECT recipe_name, calories, recipe_image FROM recipes"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val recipeNameIndex = it.getColumnIndex("recipe_name")
                val caloriesIndex = it.getColumnIndex("calories")
                val recipeImageIndex = it.getColumnIndex("recipe_image")
                if (recipeNameIndex >= 0 && caloriesIndex >= 0 && recipeImageIndex >= 0) {
                    val recipeName = it.getString(recipeNameIndex)
                    val calories = it.getString(caloriesIndex)
                    val recipeImage = it.getBlob(recipeImageIndex)
                    // Create a Recipe object with the retrieved data
                    val recipe = Recipe(recipeImage,recipeName, calories)
                    recipeList.add(recipe)
                } else {
                    // Handle the case where the column indices are not found
                }
            }
        }
        db.close()
        return recipeList
    }
    data class Exercise(val name: String, val metValue: Double)

    fun getExerciseListNamesAndMET(): List<String> {
        val exerciseListNamesAndMET = mutableListOf<String>()
        val query = "SELECT ex_name, MET_Value FROM exerciselist"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val nameIndex = cursor.getColumnIndex("ex_name")
            val metValueIndex = cursor.getColumnIndex("MET_Value")
            val name = cursor.getString(nameIndex)
            val metValue = cursor.getDouble(metValueIndex)
            val exerciseInfo = "$name, $metValue"
            exerciseListNamesAndMET.add(exerciseInfo)
        }
        cursor.close()
        db.close()
        return exerciseListNamesAndMET
    }
    fun getServingSizes(foodname: String): List<String> {
        val servingSizes = mutableListOf<String>()
        val db = this.readableDatabase
        try {
            val query = "SELECT serving_size FROM foodlist WHERE name = ?" // Use "name" column for filtering
            val cursor = db.rawQuery(query, arrayOf(foodname))
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val columnIndex = cursor.getColumnIndex("serving_size")
                        if (columnIndex != -1) {
                            val servingSize = cursor.getString(columnIndex)
                            servingSizes.add(servingSize)
                        } else {
                            Log.e("ServingSizes", "Column not found")
                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("ServingSizes", "Error retrieving serving sizes: ${e.message}")
            e.printStackTrace()
        } finally {
            db.close()
        }
        return servingSizes
    }


    fun getInitialCalories(foodname: String): Int {
        var totalCalories = 0
        val query = "SELECT calories FROM foodlist WHERE name = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(foodname))
        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("calories")
            totalCalories = cursor.getInt(columnIndex)
        }
        cursor.close()
        db.close()

        return totalCalories
    }
    fun getInitialCarbs(foodname: String): Int {
        var totalCarbs = 0
        val query = "SELECT Carbs FROM foodlist WHERE name = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(foodname))
        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("Carbs")
            totalCarbs = cursor.getInt(columnIndex)
        }
        cursor.close()
        db.close()
        return totalCarbs
    }
    fun getInitialProtein(foodname: String): Int {
        var totalProtein = 0
        val query = "SELECT Protein FROM foodlist WHERE name = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(foodname))
        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("Protein")
            totalProtein = cursor.getInt(columnIndex)
        }
        cursor.close()
        db.close()
        return totalProtein
    }
    fun getInitialFat(foodname: String): Int {
        var totalFat = 0
        val query = "SELECT Fat FROM foodlist WHERE name = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(foodname))
        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("Fat")
            totalFat = cursor.getInt(columnIndex)
        }
        cursor.close()
        db.close()
        return totalFat
    }
}

