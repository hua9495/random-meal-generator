package com.alexchan.random_meal_generator.model.api

import com.google.gson.annotations.SerializedName

data class Drinks(
    @SerializedName("drinks")
    var items: List<Drink>?
)
