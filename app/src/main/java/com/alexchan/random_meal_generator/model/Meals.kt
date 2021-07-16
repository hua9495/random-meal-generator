package com.alexchan.random_meal_generator.model

import com.google.gson.annotations.SerializedName

data class Meals(
    @SerializedName("meals")
    var items: List<Meal>?
)
