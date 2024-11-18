package com.janob.epitome.data.repository

import com.janob.epitome.data.model.BaseState
import com.janob.epitome.data.model.response.ResultResponse
import com.janob.epitome.data.model.runRemote
import com.janob.epitome.data.remote.MainApi
import okhttp3.MultipartBody
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val api: MainApi) : MainRepository {
    override suspend fun postInputMusic(music: MultipartBody.Part): BaseState<List<ResultResponse>> =
        runRemote { api.inputMusic(music) }
}