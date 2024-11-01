package com.janob.epitome.presentation.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janob.epitome.data.model.BaseState
import com.janob.epitome.data.model.response.ResultSong
import com.janob.epitome.data.repository.MainRepository
import com.janob.epitome.presentation.ui.main.result.ResultEvent
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

    private val _resultList = MutableStateFlow<List<ResultSong>>(emptyList())
    val resultList: StateFlow<List<ResultSong>> = _resultList.asStateFlow()

    private val songList: List<ResultSong> = listOf(
        ResultSong(
            name = "너랑 나",
            artistName = "IU",
            album = "너랑 나 앨범 명",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 100.0
        ),
        ResultSong(
            name = "Song Two",
            artistName = "Singer Two",
            album = "너랑 나 앨범 명2",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 80.5
        ),
        ResultSong(
            name = "Song Three",
            artistName = "Singer Three",
            album = "너랑 나 앨범 명3",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 72.5
        ),
        ResultSong(
            name = "너랑 나",
            artistName = "IU",
            album = "너랑 나 앨범 명",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 70.5
        ),
        ResultSong(
            name = "Song Two",
            artistName = "Singer Two",
            album = "너랑 나 앨범 명2",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 60.2
        ),
        ResultSong(
            name = "Song Three",
            artistName = "Singer Three",
            album = "너랑 나 앨범 명3",
            albumImgUrl = "https://i.scdn.co/image/ab67616d0000b273bf5f4138ebc9ba3fd6f0cde9",
            previewUrl = "https://p.scdn.co/mp3-preview/25cb2b3bdd7c7f0bf594d32215c00ee27645f1b9?cid=345d71b717834eb7a4c0136d98112fe0",
            similarity = 50.3
        ),
    )

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

    private fun setResultList(results: List<ResultSong>) {
        viewModelScope.launch {
            _resultList.emit(results)
        }
    }
    private fun createPartFromFile(file: File): MultipartBody.Part {
        // 파일의 MIME 타입을 설정합니다.
        val requestFile = RequestBody.create("audio/mpeg".toMediaTypeOrNull(), file)

        // MultipartBody.Part를 생성합니다.
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    fun getResultAPI(musicPath: String) {
        viewModelScope.launch {
            _event.emit(MainEvent.ShowLoading)
            setResultList(songList)
//            try {
//                val file = File(musicPath)
//                // File을 MultipartBody.Part로 변환
//                val multipartFile = createPartFromFile(file)
//
//                repository.postInputMusic(multipartFile).let {
//                    when (it) {
//                        is BaseState.Error -> {
//                            _event.emit(MainEvent.ShowToastMessage(it.msg))
//                        }
//
//                        is BaseState.Success -> {
//                            // 결과 정보 저장
//                            setResultList(it.body.result)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                _event.emit(MainEvent.DismissLoading)
//                _event.emit(MainEvent.ShowToastMessage("이미지 업로드 중 오류가 발생했습니다."))
//            }
        }
    }

    fun dismissLoading() {
        viewModelScope.launch {
            _event.emit(MainEvent.DismissLoading)
        }
    }
}