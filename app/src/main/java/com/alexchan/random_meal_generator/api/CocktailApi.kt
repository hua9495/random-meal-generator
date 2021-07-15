package com.alexchan.random_meal_generator.api

import com.alexchan.random_meal_generator.model.Drinks
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("list.php")
    fun fetchCategories(@Query("c") category: String = "list"): Observable<Drinks>

    @GET("filter.php")
    fun fetchCocktails(@Query("c") category: String = "list"): Observable<Drinks>

    @GET("lookup.php")
    fun fetchCocktailDetails(@Query("i") id: String): Observable<Drinks>
}
