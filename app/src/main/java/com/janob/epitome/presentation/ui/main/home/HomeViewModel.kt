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
    data object StartRecoding : HomeEvent()
    data object StopRecoding : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()



    fun navigateToResult() {
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigateToResult)
        }
    }

    fun onClickInputMusic(){
        viewModelScope.launch {
            if(_isRecording.value){
                _isRecording.value = false
                _event.emit(HomeEvent.StopRecoding)
            }else{
                _isRecording.value = true
                _event.emit(HomeEvent.StartRecoding)
            }

        }

    }
}