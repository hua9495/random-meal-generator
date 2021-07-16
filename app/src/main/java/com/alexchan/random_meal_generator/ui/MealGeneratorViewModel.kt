package com.alexchan.random_meal_generator.ui

import com.alexchan.random_meal_generator.core.BaseViewModel
import com.alexchan.random_meal_generator.model.Drink
import com.alexchan.random_meal_generator.model.Meal
import com.alexchan.random_meal_generator.repository.CocktailRepository
import com.alexchan.random_meal_generator.repository.MealRepository
import com.alexchan.random_meal_generator.util.guard
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class MealGeneratorViewModel(
    private val mealRepository: MealRepository,
    private val cocktailRepository: CocktailRepository
) : BaseViewModel() {

    // Store references of categories.
    private var mealCategories = listOf<String>()
    private var drinkCategories = listOf<String>()

    private var selectedMealCategory: String? = null
    private var selectDrinkCategory: String? = null

    val mealCategoryList: PublishSubject<List<String>> = PublishSubject.create()
    val drinkCategoryList: PublishSubject<List<String>> = PublishSubject.create()

    val mealCombo: BehaviorSubject<Pair<Meal, Drink>> = BehaviorSubject.create()

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

    fun fetchCategories(): Disposable {
        return subscribe(
            Observable.zip(
                mealRepository.fetchCategories(),
                cocktailRepository.fetchCategories(),
                { meals, drinks ->
                    return@zip Pair(meals, drinks)
                }
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (meals, drinks) ->
                    mealCategories = meals.map { meal ->
                        meal.category!!
                    }
                    drinkCategories = drinks.map { drink ->
                        drink.category!!
                    }
                    mealCategoryList.onNext(mealCategories)
                    drinkCategoryList.onNext(drinkCategories)
                }
        )
    }

    fun fetchRandomMealCombo(): Disposable {
        val mealCategory = selectedMealCategory.guard {
            return Disposable.empty()
        }
        val drinkCategory = selectDrinkCategory.guard {
            return Disposable.empty()
        }

        return subscribe(
            Observable.zip(
                mealRepository.fetchMeals(mealCategory),
                cocktailRepository.fetchCocktails(drinkCategory),
                { meals, drinks ->
                    return@zip Pair(meals, drinks)
                }
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (meals, drinks) ->
                    mealCombo.onNext(meals.random() to drinks.random())
                }
        )
    }
}
