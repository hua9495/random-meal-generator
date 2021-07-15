package com.alexchan.random_meal_generator.repository

import com.alexchan.random_meal_generator.api.MealApi
import com.alexchan.random_meal_generator.model.api.Meal
import io.reactivex.rxjava3.core.Observable

class MealRepository(private val mealApi: MealApi) {

    fun fetchMeals(category: String): Observable<List<Meal>> {
        return mealApi.fetchMeals(category).map {
            it.items
        }
    }

    fun fetchCategories(): Observable<List<Meal>> {
        return mealApi.fetchCategories().map {
            it.items
        }
    }

    fun fetchMealDetails(id: String): Observable<List<Meal>> {
        return mealApi.fetchMealDetails(id).map {
            it.items
        }
    }
}
