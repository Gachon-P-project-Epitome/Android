package com.janob.epitome.presentation.ui.main.result

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.janob.epitome.R
import com.janob.epitome.databinding.FragmentResultBinding
import com.janob.epitome.presentation.base.BaseFragment
import com.janob.epitome.presentation.customview.ConfirmDialog
import com.janob.epitome.presentation.customview.ConfirmDialogInterface
import com.janob.epitome.presentation.ui.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ResultFragment : BaseFragment<FragmentResultBinding>(R.layout.fragment_result),
    ConfirmDialogInterface {

    private val viewModel: ResultViewModel by viewModels()
    private lateinit var resultAdapter: ResultRVAdapter
    private lateinit var recyclerView: RecyclerView
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        initEventObserve()

        recyclerView = binding.rvResult
        resultAdapter = ResultRVAdapter(emptyList())
        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeRvViewModel()

        initResultSongs()
        setResultOnClick()

//        // 뒤로가기 버튼 클릭 이벤트 처리
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            toResearch()
//        }
    }

    private fun observeRvViewModel() {
        viewModel.resultSongs.onEach { results ->
            Log.d("ResultFragment", "observeRvViewModel ${results}")
            resultAdapter.updateResults(results) // 새로운 결과로 어댑터 업데이트
            parentViewModel.dismissLoading()
        }.launchIn(viewLifecycleOwner.lifecycleScope) // Flow를 관찰
    }



    private fun initResultSongs() {
        repeatOnStarted {
            viewModel.setResultSongs(parentViewModel.resultListState.value)
            Log.d("ResultFragment", "initResultSongs ${parentViewModel.resultListState.value}")
        }
    }

    private fun setResultOnClick() {
        // 결과 클릭 리스너
        resultAdapter.setMyItemClickListener(object : MyItemClickListener {
            override fun onItemClick(index: Int) {
                // DetailFragment로 index 값을 전달
                findNavController().toDetail(index)
            }
        })
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ResultEvent.NavigateToBack -> toResearch()
                }
            }
        }
    }

    private fun NavController.toDetail(index: Int) {
        var action = ResultFragmentDirections.actionResultFragmentToDetailFragment(index)
        navigate(action)
    }


    private fun toResearch(){
        // 재검색하러 가기 다이얼로그

        // 다이얼로그
        val title = "다시 검색하시겠습니까?"
        val content = "다시 검색하신다면 \n기존의 검색 결과는 사라집니다."
        val dialog = ConfirmDialog(this@ResultFragment, title, content, 1)
        // 알림창이 띄워져있는 동안 배경 클릭 막기
        dialog.isCancelable = false
        activity?.let { dialog.show(it.supportFragmentManager, "ConfirmDialog") }
    }

    override fun onClickYesButton(id: Int) {
        parentViewModel.deleteResultSongs()
        findNavController().navigateUp()
    }
}