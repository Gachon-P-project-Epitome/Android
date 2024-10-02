package com.janob.epitome.presentation.ui.main.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.janob.epitome.R
import com.janob.epitome.databinding.FragmentDetailBinding
import com.janob.epitome.presentation.base.BaseFragment
import com.janob.epitome.presentation.ui.main.home.HomeEvent
import com.janob.epitome.presentation.ui.main.home.HomeFragmentDirections

class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()

    }

//    private fun observeTimerViewModel() {
//
//        repeatOnStarted {
//            viewModel.currentTime.collect { currentTime ->
//                viewModel.progress.value = currentTime*100000 / 30
//            }
//        }
//    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is DetailEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun NavController.toResult() {
        var action = HomeFragmentDirections.actionHomeFragmentToResultFragment()
        navigate(action)
    }
}
