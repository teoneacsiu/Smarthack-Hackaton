package com.hailavirtual.di

import com.hailavirtual.data.repo.AuthRepositoryImpl
import com.hailavirtual.data.repo.LabRepositoryImpl
import com.hailavirtual.domain.repo.AuthRepository
import com.hailavirtual.domain.repo.LabRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLabRepository(impl: LabRepositoryImpl): LabRepository
}