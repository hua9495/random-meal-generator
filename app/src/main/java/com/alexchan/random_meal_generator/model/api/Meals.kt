package com.alexchan.random_meal_generator.model.api

import com.google.gson.annotations.SerializedName

data class Meals(
    @SerializedName("meals")
    var items: List<Meal>?
)
