package com.janob.epitome.presentation.ui.main.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.janob.epitome.R
import com.janob.epitome.databinding.FragmentResultBinding
import com.janob.epitome.presentation.base.BaseFragment

class ResultFragment : BaseFragment<FragmentResultBinding>(R.layout.fragment_result) {

    private val viewModel: ResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()

    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ResultEvent.NavigateToBack -> findNavController().navigateUp()
                    is ResultEvent.NavigateToDetail -> findNavController().toDetail()
                }
            }
        }
    }

    private fun NavController.toDetail() {
        var action = ResultFragmentDirections.actionResultFragmentToDetailFragment()
        navigate(action)
    }
}