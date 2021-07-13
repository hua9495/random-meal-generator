package com.alexchan.random_meal_generator.api

import com.alexchan.random_meal_generator.model.Meals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("list.php")
    suspend fun fetchCategories(@Query("c") category: String = "list"): Response<Meals>

    @GET("filter.php")
    suspend fun fetchMeals(@Query("c") category: String = "list"): Response<Meals>

    @GET("lookup.php")
    suspend fun fetchMealDetails(@Query("i") id: String): Response<Meals>
}
