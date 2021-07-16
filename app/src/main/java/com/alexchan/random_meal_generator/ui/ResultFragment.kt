package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.alexchan.random_meal_generator.databinding.FragmentResultBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResultFragment : Fragment() {

    private val viewModel: MealGeneratorViewModel by sharedViewModel()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onStart() {
        super.onStart()
        with(binding) {
            viewModel.fetchRandomMealCombo()
            viewModel.mealCombo
                .subscribe(
                    { (meal, drink) ->
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
