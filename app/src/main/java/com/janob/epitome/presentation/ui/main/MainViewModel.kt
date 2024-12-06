package com.janob.epitome.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janob.epitome.data.model.BaseState
import com.janob.epitome.data.model.response.ResultResponse
import com.janob.epitome.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


sealed class MainEvent {
    data object GoToGetMusic : MainEvent()
    data object StopGetMusic : MainEvent()
    data object ShowLoading : MainEvent()
    data object DismissLoading : MainEvent()
    data class ShowToastMessage(val msg: String) : MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    private val _inputMusic = MutableSharedFlow<String>()
    val inputMusic: SharedFlow<String> = _inputMusic.asSharedFlow()

    private val _resultList = MutableSharedFlow<List<ResultResponse>>()
    val resultList: SharedFlow<List<ResultResponse>> = _resultList.asSharedFlow()

    private val _resultListState = MutableStateFlow<List<ResultResponse>>(emptyList())
    val resultListState: StateFlow<List<ResultResponse>> = _resultListState.asStateFlow()

    private val _startAnimation = MutableSharedFlow<Boolean>()
    val startAnimation: SharedFlow<Boolean> = _startAnimation.asSharedFlow()

    fun controlAnimation(isStart: Boolean) {
        viewModelScope.launch {
            _startAnimation.emit(isStart)
        }
    }

    fun goToSetInputMusic() {
        viewModelScope.launch {
            _event.emit(MainEvent.GoToGetMusic)
        }
    }

    fun stopGetMusic() {
        viewModelScope.launch {
            _event.emit(MainEvent.StopGetMusic)
        }
    }

    fun setInputMusic(musicPath: String) {
        viewModelScope.launch {
            _inputMusic.emit(musicPath) // 녹음된 파일 경로를 _inputMusic에 저장
            getResultAPI(musicPath) // 녹음된 파일 경로를 이용해 api통신
        }
    }

    private fun setResultList(results: List<ResultResponse>) {
        viewModelScope.launch {
            _resultList.emit(results)
            _resultListState.value = results
        }
    }
    private fun createPartFromFile(file: File): MultipartBody.Part {
        // MP3 파일의 MIME 타입을 설정합니다.
        val requestFile = RequestBody.create("audio/mpeg".toMediaTypeOrNull(), file)

        // MultipartBody.Part를 생성합니다.
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    fun getResultAPI(musicPath: String) {
        viewModelScope.launch {
//            _event.emit(MainEvent.ShowLoading)
            try {
                val file = File(musicPath)
                if (file.exists()) {
                    // File을 MultipartBody.Part로 변환
                    val multipartFile = createPartFromFile(file)

                    repository.postInputMusic(multipartFile).let {
                        when (it) {
                            is BaseState.Error -> {
                                _event.emit(MainEvent.DismissLoading)
                                _event.emit(MainEvent.ShowToastMessage(it.msg))
                            }

                            is BaseState.Success -> {
                                // 결과 정보 저장
                                setResultList(it.body)
                            }
                        }
                    }
                } else {
                    Log.d("getResultAPI", "파일이 존재하지 않습니다.")
                }
            } catch (e: Exception) {
                _event.emit(MainEvent.DismissLoading)
                _event.emit(MainEvent.ShowToastMessage("검색 중 오류가 발생했습니다"))
            }
        }
    }

    //결과리스트 삭제 (Result->Home)
    fun deleteResultSongs(){
        _resultListState.value = emptyList()
    }


    fun dismissLoading() {
        viewModelScope.launch {
            _event.emit(MainEvent.DismissLoading)
        }
    }
}