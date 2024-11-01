package com.janob.epitome.presentation.ui.main.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.janob.epitome.R
import com.janob.epitome.databinding.FragmentHomeBinding
import com.janob.epitome.presentation.base.BaseFragment
import com.janob.epitome.presentation.ui.main.MainEvent
import com.janob.epitome.presentation.ui.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var animator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()
        observeParentViewModel()
    }

    private fun observeParentViewModel() {
        // 새로운 결과리스트 관찰
        parentViewModel.resultList.onEach { songs ->
            Log.d("HomeFragment : getResult", "parentViewModel.resultList.value = $songs")
            if (songs.isNotEmpty()){
                findNavController().toResult()
            } else {
                isEmptyResult()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope) // Flow를 관찰
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is HomeEvent.NavigateToResult -> findNavController().toResult()
                    HomeEvent.StartRecoding -> {
                        parentViewModel.goToSetInputMusic()
                        startRotation()
                    }
                    HomeEvent.StopRecoding -> {
                        stopRotation()
                        parentViewModel.stopGetMusic()
                    }
                }
            }
        }
    }

    private fun isEmptyResult(){
        // todo: 검색결과가 없으니 다시 검색하라는 다이얼로그 띄우기
        parentViewModel.dismissLoading()
    }

    private fun startRotation() {
        animator = ObjectAnimator.ofFloat(binding.btnImputMusic, "rotation", 0f, 360f)
        animator?.duration = 4000
        animator?.repeatCount = ObjectAnimator.INFINITE // 무한 반복
        animator?.start()
    }

    private fun stopRotation() {
        animator?.cancel() // 회전 애니메이션 중지
        binding.btnImputMusic.rotation = 0f // 초기 위치로 리셋 (원하는 경우)
    }
    private fun NavController.toResult() {
        var action = HomeFragmentDirections.actionHomeFragmentToResultFragment()
        navigate(action)
    }
}