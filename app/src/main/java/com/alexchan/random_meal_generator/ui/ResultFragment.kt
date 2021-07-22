package com.alexchan.random_meal_generator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.alexchan.random_meal_generator.databinding.FragmentResultBinding
import com.alexchan.random_meal_generator.ui.view_model.DefaultResultViewModel
import com.alexchan.random_meal_generator.ui.view_model.ResultViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.android.ext.android.inject

class ResultFragment : Fragment() {

    private val viewModel: DefaultResultViewModel by inject()
    private val arguments: ResultFragmentArgs by navArgs()

    private var _binding: FragmentResultBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var veGet: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private var veReload: PublishSubject<Pair<String, String>> = PublishSubject.create()
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()

        with(binding) {
            toolbar.setupWithNavController(findNavController())

            disposables.add(
                viewModel.set(
                    object : ResultViewModel.ViewEvent {
                        override val get: Observable<Pair<String, String>> = veGet
                        override val reload: Observable<Pair<String, String>> = veReload
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
                    { (meal, drink) ->
                        mealImageView.load(meal.imageUrl)
                        mealNameTextView.text = meal.name

                        drinkImageView.load(drink.imageUrl)
                        drinkNameTextView.text = drink.name
                    },
                    {}
                )
            )
        }

        veGet.onNext(arguments.mealCategory to arguments.drinkCategory)
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
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
