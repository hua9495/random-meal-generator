package com.alexchan.random_meal_generator.ui.view_model

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

class DefaultCategoriesViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mealRepository: MealRepository
) : CategoriesViewModel {

    override val output: Observable<CategoriesViewModel.Output>
        get() = oMain

    override val input: PublishSubject<CategoriesViewModel.Input>
        get() = iMain

    private val oMain: ReplaySubject<CategoriesViewModel.Output> = ReplaySubject.create(1)

    private val iMain: PublishSubject<CategoriesViewModel.Input> = PublishSubject.create()

    private val vdLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val vdResult: BehaviorSubject<Pair<List<String>, List<String>>> =
        BehaviorSubject.create()
    private val vdSelectedMeal: BehaviorSubject<String?> = BehaviorSubject.create()
    private val vdSelectedDrink: BehaviorSubject<String?> = BehaviorSubject.create()
    private val vdButtonStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private val schedulerUi: Scheduler = AndroidSchedulers.mainThread()
    private val schedulerIo: Scheduler = Schedulers.io()

    override fun set(viewEvent: CategoriesViewModel.ViewEvent): Disposable {
        val disposable = CompositeDisposable()

        disposable.add(
            Observable.merge(
                viewEvent.get,
                viewEvent.reload
            )
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap {
                    vdLoading.onNext(true)
                    return@flatMap Observable.zip(
                        mealRepository.fetchCategories(),
                        cocktailRepository.fetchCategories(),
                        { meals, drinks ->
                            val mealCategories = meals.map {
                                it.category!!
                            }
                            val drinkCategories = drinks.map {
                                it.category!!
                            }
                            return@zip Pair(mealCategories, drinkCategories)
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

        disposable.add(
            Observable.zip(
                viewEvent.selectMealCategory,
                viewEvent.selectDrinkCategory,
                { selectedMeal, selectedDrink ->
                    return@zip Pair(selectedMeal, selectedDrink)
                }
            )
                .subscribe { (selectedMeal, selectedDrink) ->
                    vdSelectedMeal.onNext(selectedMeal)
                    vdSelectedDrink.onNext(selectedDrink)
                    vdButtonStatus.onNext(selectedMeal != null && selectedDrink != null)
                }
        )

        disposable.add(
            viewEvent.generateMealCombo.flatMap {
                return@flatMap Observable.zip(
                    vdSelectedMeal,
                    vdSelectedDrink,
                    { selectedMeal, selectedDrink ->
                        return@zip Pair(selectedMeal, selectedDrink)
                    }
                )
            }
                .subscribe { (selectedMeal, selectedDrink) ->
                    oMain.onNext(
                        CategoriesViewModel.Output.NavigateToResult(
                            selectedMeal!!,
                            selectedDrink!!
                        )
                    )
                }
        )

        return disposable
    }

    override fun generateViewData(): CategoriesViewModel.ViewData {
        return object : CategoriesViewModel.ViewData {
            override val loading: Observable<Boolean> = vdLoading
            override val result: Observable<Pair<List<String>, List<String>>> = vdResult
            override val shouldEnableButton: Observable<Boolean> = vdButtonStatus
            override val selectedMeal: Observable<String?> = vdSelectedMeal
            override val selectedDrink: Observable<String?> = vdSelectedDrink
        }
    }
}
