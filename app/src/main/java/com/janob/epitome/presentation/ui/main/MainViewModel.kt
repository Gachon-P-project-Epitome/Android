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

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location.asStateFlow()


    fun controlAnimation(isStart: Boolean) {
        viewModelScope.launch {
            _startAnimation.emit(isStart)
        }
    }

    fun goToSetInputMusic(location: String) {
        _location.value = location
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

    private fun getResultAPI(musicPath: String) {
        viewModelScope.launch {
            Log.d("getResultAPI",location.value)
            when(location.value){
                "left" -> createDummyLeft()
                "right" -> createDummyRight()
                "top" -> createDummyTop()
                "bottom" -> createDummyBottom()
                "center" -> setResultList(emptyList())
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

    private fun createDummyLeft() {
        val dummyResults = listOf(
            ResultResponse(
                name = "sorry",
                artist = "Yang Da Il",
                album = "sorry",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2738d06a270bd7196322f5271c9",
                previewUrl = "/music/0eD9reMqWv79X3mAN41OhD.mp3",
                similarity = 0.46358558535575867,
                id = "0eD9reMqWv79X3mAN41OhD",
                genre = "Folk"
            ),
            ResultResponse(
                name = "Realistic City Living",
                artist = "Beta Radio",
                album = "Ancient Transition",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273edfa30099c945ed86909bbcb",
                previewUrl = "/music/0mtz7X0M37nS5JwsjElNgk.mp3",
                similarity = 0.3970928490161896,
                id = "0mtz7X0M37nS5JwsjElNgk",
                genre = "Folk"
            ),
            ResultResponse(
                name = "Mr Know It All",
                artist = "Connor Stephen",
                album = "Indie Folk Central: May 2024",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2734cbca05f9103516f30fcd1e4",
                previewUrl = "/music/1fvbF5HDHKFT9pcsSKxQio.mp3",
                similarity = 0.39596083760261536,
                id = "1fvbF5HDHKFT9pcsSKxQio",
                genre = "Folk"
            ),
            ResultResponse(
                name = "Overnight Mail: II. Standard (Live)",
                artist = "Michael Torke",
                album = "2017 WASBE International Biennial Conference: American Chamber Winds (Live)",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273aae9d3af7cf3e50de30b2ff7",
                previewUrl = "/music/2MclOkUutPoNCwkqeLxOw1.mp3",
                similarity = 0.3946313261985779,
                id = "2MclOkUutPoNCwkqeLxOw1",
                genre = "Folk"
            ),
            ResultResponse(
                name = "Tongue Tied",
                artist = "Beta Radio",
                album = "Ancient Transition",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273edfa30099c945ed86909bbcb",
                previewUrl = "/music/7FDBPudZWVZoL7I1AVZ8bw.mp3",
                similarity = 0.3939330577850342,
                id = "7FDBPudZWVZoL7I1AVZ8bw",
                genre = "Folk"
            ),
        )

        setResultList(dummyResults)
    }

    private fun createDummyRight() {
        val dummyResults = listOf(
            ResultResponse(
                name = "Hype Boy",
                artist = "NewJeans",
                album = "NewJeans 1st EP 'New Jeans'",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2739d28fd01859073a3ae6ea209",
                previewUrl = "/music/0a4MMyCrzT0En247IhqZbD.mp3",
                similarity = 0.467281311750412,
                id = "0a4MMyCrzT0En247IhqZbD",
                genre = "International"
            ),
            ResultResponse(
                name = "You Shook Me All Night Long",
                artist = "Big & Rich",
                album = "International Rock Hits - 2024 - Top Hits",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273100d8f35281e9086e81711da",
                previewUrl = "/music/4BNAE5UA9FcXpJxa54OqFk.mp3",
                similarity = 0.4070204496383667,
                id = "4BNAE5UA9FcXpJxa54OqFk",
                genre = "International"
            ),
            ResultResponse(
                name = "Summer",
                artist = "Calvin Harris",
                album = "Motion",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2738fba5806a323efd272677c4d",
                previewUrl = "/music/6YUTL4dYpB9xZO5qExPf05.mp3",
                similarity = 0.40700918436050415,
                id = "6YUTL4dYpB9xZO5qExPf05",
                genre = "International"
            ),
            ResultResponse(
                name = "Miami 2 Ibiza - Swedish House Mafia vs. Tinie Tempah",
                artist = "Swedish House Mafia",
                album = "Disc-Overy",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2733ce578a2506275604c9dbc87",
                previewUrl = "/music/5ZFVacinyPxz19eK2vTodL.mp3",
                similarity = 0.40683767199516296,
                id = "5ZFVacinyPxz19eK2vTodL",
                genre = "International"
            ),
            ResultResponse(
                name = "Don't Get Me Wrong",
                artist = "Pretenders",
                album = "International Rock Hits - 2024 - Top Hits",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273100d8f35281e9086e81711da",
                previewUrl = "/music/2GaX2fl5lJqdFMmb5KsB5l.mp3",
                similarity = 0.4061231315135956,
                id = "2GaX2fl5lJqdFMmb5KsB5l",
                genre = "International"
            ),
        )

        setResultList(dummyResults)
    }
    private fun createDummyTop() {
        val dummyResults = listOf(
            ResultResponse(
                name = "Tomorrow",
                artist = "BUZZ",
                album = "Buzz Effect",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273b1b79c8d3dd18607fe573f18",
                previewUrl = "/music/3NI9UCBelAfxPogBYeBqm6.mp3",
                similarity = 0.46171095967292786,
                id = "3NI9UCBelAfxPogBYeBqm6",
                genre = "Rock"
            ),
            ResultResponse(
                name = "Stand By Me - MTV Unplugged Live at Hull City Hall",
                artist = "Liam Gallagher",
                album = "MTV Unplugged (Live At Hull City Hall)",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2735ac1263948eb368f6148fa86",
                previewUrl = "/music/1UznvP6SQzLx7deeO5UPeW.mp3",
                similarity = 0.4035834074020386,
                id = "1UznvP6SQzLx7deeO5UPeW",
                genre = "Rock"
            ),
            ResultResponse(
                name = "american dream",
                artist = "LCD Soundsystem",
                album = "american dream",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273945696d01c650eeade335ac9",
                previewUrl = "/music/60k0xRw1oYUwZYp4e2iue8.mp3",
                similarity = 0.4023647606372833,
                id = "60k0xRw1oYUwZYp4e2iue8",
                genre = "Rock"
            ),
            ResultResponse(
                name = "Don't Go Away - Remastered",
                artist = "Oasis",
                album = "Be Here Now (Deluxe Remastered Edition)",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273aeda362a434f01d0eff70b4e",
                previewUrl = "/music/1Z5Uc9jMuuVQxtRZTforfX.mp3",
                similarity = 0.4012387692928314,
                id = "1Z5Uc9jMuuVQxtRZTforfX",
                genre = "Rock"
            ),
            ResultResponse(
                name = "The Life of Riley",
                artist = "The Lightning Seeds",
                album = "Sense",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2731b9a2cf19b38bed0e881df0f",
                previewUrl = "/music/5fB91ioIvFnr6QEgXLCf3V.mp3",
                similarity = 0.40076419711112976,
                id = "5fB91ioIvFnr6QEgXLCf3V",
                genre = "Rock"
            ),
        )

        setResultList(dummyResults)
    }

    private fun createDummyBottom() {
        val dummyResults = listOf(
            ResultResponse(
                name = "Havana (feat. Young Thug)",
                artist = "Camila Cabello",
                album = "Camila",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2736eb0b9e73adcf04e4ed3eca4",
                previewUrl = "/music/1rfofaqEpACxVEHIZBJe6W.mp3",
                similarity = 0.4667615294456482,
                id = "1rfofaqEpACxVEHIZBJe6W",
                genre = "Pop"
            ),
            ResultResponse(
                name = "Havana - Remix",
                artist = "Camila Cabello",
                album = "Havana (Remix)",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2735556fb4dfea9c05e09519c25",
                previewUrl = "/music/3whrwq4DtvucphBPUogRuJ.mp3",
                similarity = 0.40286192297935486,
                id = "3whrwq4DtvucphBPUogRuJ",
                genre = "Pop"
            ),
            ResultResponse(
                name = "a thousand years",
                artist = "Christina Perri",
                album = "a thousand years",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2733dea4a2ccd58ad1f8e4dbb03",
                previewUrl = "/music/6lanRgr6wXibZr8KgzXxBl.mp3",
                similarity = 0.4028056263923645,
                id = "6lanRgr6wXibZr8KgzXxBl",
                genre = "Pop"
            ),
            ResultResponse(
                name = "I'm Good (Blue)",
                artist = "David Guetta",
                album = "I'm Good (Blue)",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b273933c036cd61cd40d3f17a9c4",
                previewUrl = "/music/4uUG5RXrOk84mYEfFvj3cK.mp3",
                similarity = 0.40224698185920715,
                id = "4uUG5RXrOk84mYEfFvj3cK",
                genre = "Pop"
            ),
            ResultResponse(
                name = "10,000 Hours (with Justin Bieber)",
                artist = "Dan + Shay",
                album = "Good Things",
                albumImageUrl = "https://i.scdn.co/image/ab67616d0000b2735d847661ee74db6591c4a30b",
                previewUrl = "/music/4j5ffIFh7bFT7GZciP1TCy.mp3",
                similarity = 0.4010058045387268,
                id = "4j5ffIFh7bFT7GZciP1TCy",
                genre = "Pop"
            ),
        )

        setResultList(dummyResults)
    }
}