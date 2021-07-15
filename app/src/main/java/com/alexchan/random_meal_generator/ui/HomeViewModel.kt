package com.alexchan.random_meal_generator.ui

import androidx.lifecycle.ViewModel
import com.alexchan.random_meal_generator.repository.CocktailRepository
import com.alexchan.random_meal_generator.repository.MealRepository
import io.reactivex.rxjava3.core.Observable

class HomeViewModel(
    private val mealRepository: MealRepository,
    private val cocktailRepository: CocktailRepository
) : ViewModel() {

    // Store references of categories.
    private var mealCategories = listOf<String>()
    private var drinkCategories = listOf<String>()

    var selectedMealCategory: String? = null
    var selectDrinkCategory: String? = null

    val shouldEnableButton: Boolean
        get() {
            return selectedMealCategory != null && selectDrinkCategory != null
        }

    fun setMealCategory(index: Int) {
        selectedMealCategory = mealCategories[index]
    }

    fun setDrinkCategory(index: Int) {
        selectDrinkCategory = drinkCategories[index]
    }

    fun getMealCategories(): Observable<List<String>> {
        return mealRepository.fetchCategories()
            .map {
                val categories = it.map { drink ->
                    drink.category!!
                }
                categories
            }
            .doOnNext {
                mealCategories = it
            }
    }

    fun getDrinkCategories(): Observable<List<String>> {
        return cocktailRepository.fetchCategories()
            .map {
                val categories = it.map { drink ->
                    drink.category!!
                }
                categories
            }
            .doOnNext {
                drinkCategories = it
            }
    }
}
