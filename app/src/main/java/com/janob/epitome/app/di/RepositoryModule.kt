package com.janob.epitome.app.di

import com.janob.epitome.data.repository.MainRepository
import com.janob.epitome.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMainRepository(globalRepositoryImpl: MainRepositoryImpl): MainRepository
}