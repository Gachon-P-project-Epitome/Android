package com.janob.epitome.presentation.ui.main.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janob.epitome.data.model.response.ResultSong
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
    data object NavigateToDetail : ResultEvent()
    data object ShowLoading : ResultEvent()
    data object DismissLoading : ResultEvent()
}

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel()  {


    private val _event = MutableSharedFlow<ResultEvent>()
    val event: SharedFlow<ResultEvent> = _event.asSharedFlow()

    private val _resultSongs = MutableStateFlow<List<ResultSong>>(emptyList())
    val resultSongs: StateFlow<List<ResultSong>> = _resultSongs.asStateFlow()

    private val _isEmpty = MutableStateFlow(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty.asStateFlow()




    fun checkResultsIsEmpty() {
        _isEmpty.value = _resultSongs.value.isEmpty()
    }

    fun setResultSongs(songs: List<ResultSong>) {
            _resultSongs.value = songs
    }

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
