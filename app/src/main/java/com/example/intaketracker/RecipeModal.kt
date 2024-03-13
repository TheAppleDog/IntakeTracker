package com.example.intaketracker

class RecipeModal (private var recipe_name: String, private var recipe_calories: String, private var recipe_image: ByteArray) {

    // Getter and Setter
    fun getRecipe_name(): String {
        return recipe_name
    }

    fun setRecipe_name(recipe_name: String) {
        this.recipe_name = recipe_name
    }

    fun getRecipe_calories(): String {
        return recipe_calories
    }

    fun setRecipe_calories(recipe_calories: String) {
        this.recipe_calories = recipe_calories
    }

    fun getRecipe_image(): ByteArray {
        return recipe_image
    }

    fun setRecipe_image(recipe_image: ByteArray) {
        this.recipe_image = recipe_image
    }
}
