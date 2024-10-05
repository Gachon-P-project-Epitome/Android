package com.janob.epitome.data.remote

import com.janob.epitome.data.model.response.ResultResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MainApi {

//    @POST("review/create")
//    suspend fun createReview(
//        @Body params: CreateReviewRequest
//    ): Response<CreateReviewResponse>

    @Multipart
    @POST("record")
    suspend fun inputMusic(
        @Part music: MultipartBody.Part
    ): Response<ResultResponse>
}