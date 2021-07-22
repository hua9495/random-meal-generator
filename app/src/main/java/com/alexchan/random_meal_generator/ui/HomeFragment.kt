package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alexchan.random_meal_generator.R
import com.alexchan.random_meal_generator.databinding.FragmentHomeBinding
import com.alexchan.random_meal_generator.ui.view_model.CategoriesViewModel
import com.alexchan.random_meal_generator.ui.view_model.DefaultCategoriesViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val viewModel: DefaultCategoriesViewModel by inject()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mealsAdapter: ArrayAdapter<String>
    private lateinit var drinksAdapter: ArrayAdapter<String>

    private var veGet: PublishSubject<Unit> = PublishSubject.create()
    private var veReload: PublishSubject<Unit> = PublishSubject.create()
    private var veSelectMealCategory: PublishSubject<String> = PublishSubject.create()
    private var veSelectDrinkCategory: PublishSubject<String> = PublishSubject.create()
    private var veGenerateMealCombo: PublishSubject<Unit> = PublishSubject.create()
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()

        with(binding) {
            toolbar.setupWithNavController(findNavController())

            disposables.add(
                viewModel.set(
                    object : CategoriesViewModel.ViewEvent {
                        override val get: Observable<Unit> = veGet
                        override val reload: Observable<Unit> = veReload
                        override val selectMealCategory: Observable<String?> = veSelectMealCategory
                        override val selectDrinkCategory: Observable<String?> =
                            veSelectDrinkCategory
                        override val generateMealCombo: Observable<Unit> = veGenerateMealCombo
                    }
                )
            )

            val viewData = viewModel.generateViewData()
            disposables.add(
                viewData.loading.subscribe(
                    {
                        progressBar.isVisible = it
                        contentLayout.isVisible = !it
                    },
                    {}
                )
            )

            disposables.add(
                viewData.result.subscribe(
                    { (mealCategories, drinkCategories) ->
                        mealsAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.category_list_item,
                            mealCategories
                        )
                        mealsSelectionTextView.setAdapter(mealsAdapter)

                        drinksAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.category_list_item,
                            drinkCategories
                        )
                        drinksSelectionTextView.setAdapter(drinksAdapter)
                    },
                    {}
                )
            )

            disposables.add(
                viewData.shouldEnableButton.subscribe {
                    generateButton.isEnabled = it
                }
            )

            disposables.add(
                viewModel.output.subscribe(
                    {
                        when (it) {
                            is CategoriesViewModel.Output.NavigateToResult -> {
                                val action = HomeFragmentDirections.actionHomeFragmentToResultFragment(
                                    mealCategory = it.selectedMealCategory,
                                    drinkCategory = it.selectedDrinkCategory
                                )
                                findNavController().navigate(action)
                            }
                        }
                    },
                    {}
                )
            )

            mealsSelectionTextView.setOnItemClickListener { _, _, index, _ ->
                veSelectMealCategory.onNext(mealsAdapter.getItem(index))
            }

            drinksSelectionTextView.setOnItemClickListener { _, _, index, _ ->
                veSelectDrinkCategory.onNext(drinksAdapter.getItem(index))
            }

            generateButton.setOnClickListener {
                veGenerateMealCombo.onNext(Unit)
            }
        }
        veGet.onNext(Unit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
