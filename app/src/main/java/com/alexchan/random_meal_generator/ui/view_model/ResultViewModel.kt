package com.alexchan.random_meal_generator.ui.view_model

import com.alexchan.random_meal_generator.model.Drink
import com.alexchan.random_meal_generator.model.Meal
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

interface ResultViewModel {

    val output: Observable<Output>
    val input: PublishSubject<Input>
    fun set(viewEvent: ViewEvent): Disposable
    fun generateViewData(): ViewData

    interface ViewEvent {
        val get: Observable<Pair<String, String>>
        val reload: Observable<Pair<String, String>>
    }

    interface ViewData {
        val loading: Observable<Boolean>
        val result: Observable<Pair<Meal, Drink>>
    }

    sealed class Output {
        object NavigateToDetail : Output()
    }

    sealed class Input {
        object ScrollToTop : Input()
    }
}
