package com.alexchan.random_meal_generator.model

import com.google.gson.annotations.SerializedName

data class Drink(
    @SerializedName("idDrink")
    var id: String?,
    @SerializedName("strDrink")
    var name: String?,
    @SerializedName("strDrinkThumb")
    val imageUrl: String?,
    @SerializedName("strCategory")
    val category: String?,
    @SerializedName("strInstructions")
    val description: String?
)
