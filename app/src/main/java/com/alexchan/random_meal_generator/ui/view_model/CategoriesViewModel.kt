package com.alexchan.random_meal_generator.ui.view_model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

interface CategoriesViewModel {

    val output: Observable<Output>
    val input: PublishSubject<Input>
    fun set(viewEvent: ViewEvent): Disposable
    fun generateViewData(): ViewData

    interface ViewEvent {
        val get: Observable<Unit>
        val reload: Observable<Unit>
        val selectMealCategory: Observable<String?>
        val selectDrinkCategory: Observable<String?>
        val generateMealCombo: Observable<Unit>
    }

    interface ViewData {
        val loading: Observable<Boolean>
        val result: Observable<Pair<List<String>, List<String>>>
        val shouldEnableButton: Observable<Boolean>
        val selectedMeal: Observable<String?>
        val selectedDrink: Observable<String?>
    }

    sealed class Output {
        class NavigateToResult(
            val selectedMealCategory: String,
            val selectedDrinkCategory: String,
        ) : Output()
    }

    sealed class Input {
        object ScrollToTop : Input()
    }
}
