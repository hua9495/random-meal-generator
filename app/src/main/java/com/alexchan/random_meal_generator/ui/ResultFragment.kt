package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.alexchan.random_meal_generator.core.BaseFragment
import com.alexchan.random_meal_generator.databinding.FragmentResultBinding
import com.alexchan.random_meal_generator.model.ui.MealCombo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResultFragment : BaseFragment() {

    private val viewModel: MealGeneratorViewModel by sharedViewModel()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onStart() {
        super.onStart()
        with(binding) {
            Observable.zip(
                viewModel.getRandomMeal(),
                viewModel.getRandomDrink(),
                { meal, drink ->
                    return@zip MealCombo(meal, drink)
                }
            )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        val meal = it.meal
                        val drink = it.drink

                        mealImageView.load(meal.imageUrl)
                        mealNameTextView.text = meal.name

                        drinkImageView.load(drink.imageUrl)
                        drinkNameTextView.text = drink.name
                    },
                    {
                        Log.d(this@ResultFragment.tag, it.message.toString())
                    }
                )

            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
