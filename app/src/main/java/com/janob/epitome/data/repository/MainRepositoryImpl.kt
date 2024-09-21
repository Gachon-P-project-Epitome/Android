package com.janob.epitome.data.repository

import com.janob.epitome.data.remote.MainApi
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val api: MainApi) : MainRepository {

//    override suspend fun createReview(params: CreateReviewRequest): BaseState<CreateReviewResponse> =
//        runRemote { api.createReview(params)}
}