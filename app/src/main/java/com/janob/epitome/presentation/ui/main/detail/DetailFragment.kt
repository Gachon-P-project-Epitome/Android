package com.janob.epitome.presentation.ui.main.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.janob.epitome.R
import com.janob.epitome.databinding.FragmentDetailBinding
import com.janob.epitome.presentation.base.BaseFragment
import com.janob.epitome.presentation.ui.main.MainViewModel
import com.janob.epitome.presentation.ui.main.home.HomeEvent
import com.janob.epitome.presentation.ui.main.home.HomeFragmentDirections

class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var index: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
        arguments?.let {
            index = it.getInt("index") // index 값 받기
        }
        viewModel.getResultSong(parentViewModel.resultListState.value[index])
        observeViewModel()
    }

//    private fun observeTimerViewModel() {
//
//        repeatOnStarted {
//            viewModel.currentTime.collect { currentTime ->
//                viewModel.progress.value = currentTime*100000 / 30
//            }
//        }
//    }

    private fun observeViewModel() {
        // 프로필 이미지 관찰
        repeatOnStarted {
            viewModel.albumImg.collect { imageUrl ->
                // Glide를 사용하여 이미지 로드
                Glide.with(this@DetailFragment)
                    .load(imageUrl)
                    .error(R.drawable.ic_profile) // 오류 발생 시 표시할 이미지
                    .into(binding.itemSongcoverCoverimgIv)
            }
        }
    }

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
