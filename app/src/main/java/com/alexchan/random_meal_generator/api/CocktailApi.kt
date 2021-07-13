package com.alexchan.random_meal_generator.api

import com.alexchan.random_meal_generator.model.Drinks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("list.php")
    suspend fun fetchCategories(@Query("c") category: String = "list"): Response<Drinks>

    @GET("filter.php")
    suspend fun fetchCocktails(@Query("c") category: String = "list"): Response<Drinks>

    @GET("lookup.php")
    suspend fun fetchCocktailDetails(@Query("i") id: String): Response<Drinks>
}
