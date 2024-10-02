package com.janob.epitome.presentation.ui.main.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DetailEvent {
    data object NavigateToBack : DetailEvent()
}

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<DetailEvent>()
    val event: SharedFlow<DetailEvent> = _event.asSharedFlow()

    private val _isPlaying = MutableStateFlow<Boolean>(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    var title: MutableStateFlow<String> = MutableStateFlow("Title")
    var singer: MutableStateFlow<String> = MutableStateFlow("Singer")
    var albumTitle: MutableStateFlow<String> = MutableStateFlow("AlbumTitle")
    var progress: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _currentTime = MutableStateFlow<String>("00:00")
    val currentTime: StateFlow<String> get() = _currentTime

    private var job: Job? = null

    fun onClickPlay(){
        if(_isPlaying.value){
            _isPlaying.value = false
            stopTimer()
        } else{
            _isPlaying.value = true
            startTimer()
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(DetailEvent.NavigateToBack)
        }
    }

    private fun startTimer() {
        job = CoroutineScope(Dispatchers.Main).launch {
            var second = 0
            var mills = 0f

            while (second < 30) {
                delay(50) // 50ms 대기
                mills += 50
                _currentTime.value = String.format("%02d:%02d", second / 60, second % 60)
                progress.value = mills.toInt()*100/30

                if (mills % 1000 == 0f) { // 1초가 지났을 때
                    second++
                }
            }
            // 타이머 종료 후 처리
            _currentTime.value = "00:00"
            progress.value = 0
        }
    }

    private fun stopTimer() {
        job?.cancel()
    }
}