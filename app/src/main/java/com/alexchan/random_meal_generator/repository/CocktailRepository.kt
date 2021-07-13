package com.alexchan.random_meal_generator.repository

import com.alexchan.random_meal_generator.api.CocktailApi
import com.alexchan.random_meal_generator.core.Result
import com.alexchan.random_meal_generator.model.Drink

class CocktailRepository(private val cocktailApi: CocktailApi) {

    suspend fun fetchCocktails(category: String): Result<List<Drink>> {
        val response = cocktailApi.fetchCocktails(category)
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Cocktails!"))
        }
    }

    suspend fun fetchCategories(): Result<List<Drink>> {
        val response = cocktailApi.fetchCategories()
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Categories!"))
        }
    }

    suspend fun fetchCocktailDetails(id: String): Result<List<Drink>> {
        val response = cocktailApi.fetchCocktailDetails(id)
        return if (response.isSuccessful) {
            Result.Success(response.body()?.items!!)
        } else {
            Result.Failure(Throwable("Error fetching Cocktail Details!"))
        }
    }
}
