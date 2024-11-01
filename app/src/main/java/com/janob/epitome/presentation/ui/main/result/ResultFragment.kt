package com.janob.epitome.presentation.ui.main.result

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.janob.epitome.R
import com.janob.epitome.data.model.response.ResultSong
import com.janob.epitome.databinding.FragmentResultBinding
import com.janob.epitome.presentation.base.BaseFragment
import com.janob.epitome.presentation.ui.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ResultFragment : BaseFragment<FragmentResultBinding>(R.layout.fragment_result) {

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
            viewModel.setResultSongs(parentViewModel.resultList.value)
            Log.d("ResultFragment", "initResultSongs ${parentViewModel.resultList.value}")
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
                    is ResultEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun NavController.toDetail(index: Int) {
        var action = ResultFragmentDirections.actionResultFragmentToDetailFragment(index)
        navigate(action)
    }
}