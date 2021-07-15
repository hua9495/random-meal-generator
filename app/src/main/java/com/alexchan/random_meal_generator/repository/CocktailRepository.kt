package com.alexchan.random_meal_generator.repository

import com.alexchan.random_meal_generator.api.CocktailApi
import com.alexchan.random_meal_generator.model.Drink
import io.reactivex.rxjava3.core.Observable

class CocktailRepository(private val cocktailApi: CocktailApi) {

    fun fetchCocktails(category: String): Observable<List<Drink>> {
        return cocktailApi.fetchCocktails(category).map {
            it.items
        }
    }

    fun fetchCategories(): Observable<List<Drink>> {
        return cocktailApi.fetchCategories().map {
            it.items
        }
    }

    fun fetchCocktailDetails(id: String): Observable<List<Drink>> {
        return cocktailApi.fetchCocktailDetails(id).map {
            it.items
        }
    }
}
