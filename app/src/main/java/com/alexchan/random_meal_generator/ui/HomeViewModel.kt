package com.alexchan.random_meal_generator.ui

import androidx.lifecycle.ViewModel
import com.alexchan.random_meal_generator.repository.CocktailRepository

class HomeViewModel(private val cocktailRepository: CocktailRepository) : ViewModel()
