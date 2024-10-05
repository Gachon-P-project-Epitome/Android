package com.janob.epitome.data.repository

import com.janob.epitome.data.model.BaseState
import com.janob.epitome.data.model.response.ResultResponse
import okhttp3.MultipartBody

interface MainRepository {

    suspend fun postInputMusic(
        music: MultipartBody.Part
    ): BaseState<ResultResponse>
}