package com.alexchan.random_meal_generator.repository

import com.alexchan.random_meal_generator.api.MealApi
import com.alexchan.random_meal_generator.core.Result
import com.alexchan.random_meal_generator.model.Meal

class MealRepository(private val mealApi: MealApi) {

    suspend fun fetchMeals(category: String): Result<List<Meal>> {
        val response = mealApi.fetchMeals(category)
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Meals!"))
        }
    }

    suspend fun fetchCategories(): Result<List<Meal>> {
        val response = mealApi.fetchCategories()
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Categories!"))
        }
    }

    suspend fun fetchMealDetails(id: String): Result<List<Meal>> {
        val response = mealApi.fetchMealDetails(id)
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Meal Details!"))
        }
    }
}
