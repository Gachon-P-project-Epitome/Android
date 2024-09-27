package com.janob.epitome.presentation.ui.main.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ResultEvent {
    data object NavigateToBack : ResultEvent()
    data object NavigateToDetail : ResultEvent()
}

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<ResultEvent>()
    val event: SharedFlow<ResultEvent> = _event.asSharedFlow()



    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(ResultEvent.NavigateToBack)
        }
    }

    fun navigateToDetail() {
        viewModelScope.launch {
            _event.emit(ResultEvent.NavigateToDetail)
        }
    }
}
