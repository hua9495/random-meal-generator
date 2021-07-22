package com.alexchan.random_meal_generator.ui.view_model

import com.alexchan.random_meal_generator.model.Drink
import com.alexchan.random_meal_generator.model.Meal
import com.alexchan.random_meal_generator.repository.CocktailRepository
import com.alexchan.random_meal_generator.repository.MealRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

class DefaultResultViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mealRepository: MealRepository
) : ResultViewModel {

    override val output: Observable<ResultViewModel.Output>
        get() = oMain

    override val input: PublishSubject<ResultViewModel.Input>
        get() = iMain

    private val oMain: ReplaySubject<ResultViewModel.Output> = ReplaySubject.create(1)

    private val iMain: PublishSubject<ResultViewModel.Input> = PublishSubject.create()

    private val vdLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val vdResult: BehaviorSubject<Pair<Meal, Drink>> = BehaviorSubject.create()

    private val schedulerUi: Scheduler = AndroidSchedulers.mainThread()
    private val schedulerIo: Scheduler = Schedulers.io()

    override fun set(viewEvent: ResultViewModel.ViewEvent): Disposable {
        val disposable = CompositeDisposable()

        disposable.add(
            Observable.merge(
                viewEvent.get,
                viewEvent.reload
            )
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap { (mealCategory, drinkCategory) ->
                    vdLoading.onNext(true)
                    return@flatMap Observable.zip(
                        mealRepository.fetchMeals(mealCategory),
                        cocktailRepository.fetchCocktails(drinkCategory),
                        { meals, drinks ->
                            return@zip Pair(meals.random(), drinks.random())
                        }
                    )
                }
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .doAfterNext {
                    vdLoading.onNext(false)
                }
                .subscribe {
                    vdResult.onNext(it)
                }
        )

        return disposable
    }

    override fun generateViewData(): ResultViewModel.ViewData {
        return object : ResultViewModel.ViewData {
            override val loading: Observable<Boolean> = vdLoading
            override val result: Observable<Pair<Meal, Drink>> = vdResult
        }
    }
}
