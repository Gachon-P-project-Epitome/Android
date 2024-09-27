package com.janob.epitome.presentation.ui.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DetailEvent {
    data object NavigateToBack : DetailEvent()
}

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<DetailEvent>()
    val event: SharedFlow<DetailEvent> = _event.asSharedFlow()



    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(DetailEvent.NavigateToBack)
        }
    }

}