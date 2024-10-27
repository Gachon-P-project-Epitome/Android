package com.janob.epitome.data.model.response

data class ResultResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<ResultSong>
)

data class ResultSong(
    val name : String,
    val artistName : String,
    val album : String,
    val albumImgUrl : String,
    val previewUrl : String,
    val similarity : Double
)