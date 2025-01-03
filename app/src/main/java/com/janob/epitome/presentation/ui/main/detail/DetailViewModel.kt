package com.janob.epitome.presentation.ui.main.detail

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janob.epitome.data.model.response.ResultResponse
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

    private val _title = MutableStateFlow("Title")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _singer = MutableStateFlow("Singer")
    val singer: StateFlow<String> = _singer.asStateFlow()

    private val _albumImg = MutableStateFlow("")
    val albumImg: StateFlow<String> = _albumImg.asStateFlow()

    private val _albumTitle = MutableStateFlow("AlbumTitle")
    val albumTitle: StateFlow<String> = _albumTitle.asStateFlow()

    var progress: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _currentTime = MutableStateFlow<String>("00:00")
    val currentTime: StateFlow<String> get() = _currentTime

    private val _url = MutableStateFlow<String>("")
    val url: StateFlow<String> get() = _url

    private var job: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isTimerRunning = false
    private var currentPosition: Int = 0 // 현재 재생 위치를 저장할 변수

    fun getResultSong(song: ResultResponse){
        _title.value = song.name
        _singer.value = song.artist
        _albumImg.value = song.albumImageUrl
        _url.value = song.previewUrl
    }

    fun onClickPlay(){
        if(_isPlaying.value){
            _isPlaying.value = false
            pauseTimer() // 타이머를 멈추는 메서드 호출
            mediaPlayer?.pause()
            currentPosition = mediaPlayer?.currentPosition ?: 0 // 현재 위치 저장
        } else{
            _isPlaying.value = true
            startTimer()
            startMusic("http://210.102.178.190:5900"+_url.value)
        }
    }


    private fun startTimer() {
        if (isTimerRunning) return // 이미 타이머가 실행 중이면 시작하지 않음

        isTimerRunning = true
        job = CoroutineScope(Dispatchers.Main).launch {
            var second = 0
            var mills = 0f

            while (second < 30) {
                delay(50) // 50ms 대기
                mills += 50
                _currentTime.value = String.format("%02d:%02d", second / 60, second % 60)
                progress.value = (mills.toInt() * 100 / 30)

                if (mills % 1000 == 0f) { // 1초가 지났을 때
                    second++
                }

                // 일시 정지된 경우 대기
                while (!_isPlaying.value) {
                    delay(50) // 잠깐 대기
                }
            }
            // 타이머 종료 후 처리
            stopMusic()
            _currentTime.value = "00:00"
            progress.value = 0
            _isPlaying.value = false
            isTimerRunning = false // 타이머 상태 업데이트
        }
    }

    private fun pauseTimer() {
        // 타이머를 일시 정지 상태로 설정
        _isPlaying.value = false
    }

    private fun stopTimer() {
        job?.cancel()
        isTimerRunning = false // 타이머 상태 업데이트
    }

    private fun startMusic(url: String) {
        if (currentPosition > 0){
            mediaPlayer?.start()
        }else{
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepare() // 비동기 준비
                start()
            }
        }
    }

    private fun stopMusic() {
        currentPosition = 0
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        stopMusic()
    }
    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(DetailEvent.NavigateToBack)
        }
    }
}