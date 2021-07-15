package com.alexchan.random_meal_generator.model.api

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal")
    var id: String?,
    @SerializedName("strMeal")
    var name: String?,
    @SerializedName("strMealThumb")
    val imageUrl: String?,
    @SerializedName("strCategory")
    val category: String?,
    @SerializedName("strInstructions")
    val description: String?
)
