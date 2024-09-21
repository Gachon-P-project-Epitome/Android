package com.janob.epitome.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
//
//    @Singleton
//    @Provides
//    fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)
}