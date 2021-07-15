package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.alexchan.random_meal_generator.R
import com.alexchan.random_meal_generator.core.BaseFragment
import com.alexchan.random_meal_generator.databinding.FragmentHomeBinding
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onStart() {
        super.onStart()
        with(binding) {
            // Subscribe drinks category.
            subscribe(
                viewModel.getDrinkCategories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single())
                    .subscribe(
                        {
                            val adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.category_list_item,
                                it
                            )
                            drinksSelectionTextView.setAdapter(adapter)
                        },
                        {
                            // TODO: Add error handling.
                        }
                    )
            )

            // Subscribe meal category.
            subscribe(
                viewModel.getMealCategories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.single())
                    .subscribe(
                        {
                            val adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.category_list_item,
                                it
                            )
                            mealsSelectionTextView.setAdapter(adapter)
                        },
                        {
                            // TODO: Add error handling.
                        }
                    )
            )
        }
    }

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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun updateViews() = with(binding) {
        generateButton.isEnabled = viewModel.shouldEnableButton
    }
}
