package com.alexchan.random_meal_generator.api

import com.alexchan.random_meal_generator.model.Meals
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("list.php")
    fun fetchCategories(@Query("c") category: String = "list"): Observable<Meals>

    @GET("filter.php")
    fun fetchMeals(@Query("c") category: String = "list"): Observable<Meals>

    @GET("lookup.php")
    fun fetchMealDetails(@Query("i") id: String): Observable<Meals>
}
