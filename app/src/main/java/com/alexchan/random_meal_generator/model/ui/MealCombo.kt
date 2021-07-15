package com.alexchan.random_meal_generator.model.ui

import com.alexchan.random_meal_generator.model.api.Drink
import com.alexchan.random_meal_generator.model.api.Meal

data class MealCombo(
    var meal: Meal,
    var drink: Drink
)
