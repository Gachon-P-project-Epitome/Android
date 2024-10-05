package com.janob.epitome.data.model.response

data class ResultResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<ResultSong>
)

data class ResultSong(
    val title : String,
    val singer : String,
    val url : String,
    val albumImg : String,
)