package com.alexchan.random_meal_generator.model

import com.google.gson.annotations.SerializedName

data class Drinks(
    @SerializedName("drinks")
    var items: List<Drink>?
)
