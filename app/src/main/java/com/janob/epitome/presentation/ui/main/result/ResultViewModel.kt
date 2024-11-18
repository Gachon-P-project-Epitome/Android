package com.janob.epitome.presentation.ui.main.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janob.epitome.data.model.response.ResultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ResultEvent {
    data object NavigateToBack : ResultEvent()
}

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<ResultEvent>()
    val event: SharedFlow<ResultEvent> = _event.asSharedFlow()

    private val _resultSongs = MutableStateFlow<List<ResultResponse>>(emptyList())
    val resultSongs: StateFlow<List<ResultResponse>> = _resultSongs.asStateFlow()

    fun setResultSongs(songs: List<ResultResponse>) {
        Log.d("ResultViewModel", "setResultSongs")
            _resultSongs.value = songs
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(ResultEvent.NavigateToBack)
        }
    }
}
