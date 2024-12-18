package com.janob.epitome.data.model.response

//data class ResultResponse(
//    val isSuccess: Boolean,
//    val code: String,
//    val message: String,
//    val result: List<ResultSong>
//)

data class ResultResponse(
    val name : String,
    val artist : String,
    val album : String,
    val albumImageUrl : String,
    val previewUrl : String,
    val similarity : Double,
    val id : String,
    val genre : String,
)