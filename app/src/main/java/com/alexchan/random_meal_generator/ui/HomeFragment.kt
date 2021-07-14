package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.alexchan.random_meal_generator.R
import com.alexchan.random_meal_generator.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val mealAdapter = ArrayAdapter(
                requireContext(),
                R.layout.category_list_item,
                viewModel.mealCategories
            )
            val drinkAdapter = ArrayAdapter(
                requireContext(),
                R.layout.category_list_item,
                viewModel.drinkCategories
            )
            mealsSelectionTextView.setAdapter(mealAdapter)
            drinksSelectionTextView.setAdapter(drinkAdapter)

            mealsSelectionTextView.setOnItemClickListener { _, _, index, _ ->
                viewModel.setMealCategory(index)
                updateViews()
            }
            drinksSelectionTextView.setOnItemClickListener { _, _, index, _ ->
                viewModel.setDrinkCategory(index)
                updateViews()
            }
        }
    }

    fun updateViews() = with(binding) {
        generateButton.isEnabled =
            viewModel.selectedMealCategory != null && viewModel.selectDrinkCategory != null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
