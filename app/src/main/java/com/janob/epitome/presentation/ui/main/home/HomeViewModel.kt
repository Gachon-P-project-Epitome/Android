package com.janob.epitome.presentation.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class HomeEvent {
    data object NavigateToResult : HomeEvent()
    data class StartRecoding(val location: String) : HomeEvent() // location 추가
    data object StopRecoding : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()


    private val _location = MutableStateFlow<String>("")
    val location: StateFlow<String> get() = _location

    fun setLocation(newLocation: String) {
        _location.value = newLocation
    }

    fun onClickInputMusic(location: String) {
        viewModelScope.launch {

            if (_isRecording.value) {
                _isRecording.value = false
                _event.emit(HomeEvent.StopRecoding)
            } else {
                // location 값을 직접 설정
                _location.value = location
                _isRecording.value = true
                // location 값을 사용하여 이벤트 발송
                _event.emit(HomeEvent.StartRecoding(location))
            }
        }
    }


}