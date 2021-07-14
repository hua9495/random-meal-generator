package com.alexchan.random_meal_generator.ui

import androidx.lifecycle.ViewModel
import com.alexchan.random_meal_generator.repository.CocktailRepository

class HomeViewModel(private val cocktailRepository: CocktailRepository) : ViewModel() {

    val mealCategories = listOf("Material", "Design", "Components", "Android")

    val drinkCategories = listOf("Cock", "Tail")

    var selectedMealCategory: String? = null

    var selectDrinkCategory: String? = null

    fun setMealCategory(index: Int) {
        selectedMealCategory = mealCategories[index]
    }

    fun setDrinkCategory(index: Int) {
        selectDrinkCategory = drinkCategories[index]
    }
}
