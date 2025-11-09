package com.hailavirtual.di

import com.hailavirtual.data.repo.AuthRepositoryImpl
import com.hailavirtual.data.repo.ClassRepositoryImpl
import com.hailavirtual.data.repo.LabRepositoryImpl
import com.hailavirtual.data.repo.LessonRepositoryImpl
import com.hailavirtual.domain.repo.AuthRepository
import com.hailavirtual.domain.repo.ClassRepository
import com.hailavirtual.domain.repo.LabRepository
import com.hailavirtual.domain.repo.LessonRepository
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

    @Binds @Singleton
    abstract fun bindLessonRepository(impl: LessonRepositoryImpl): LessonRepository

    @Binds
    @Singleton
    abstract fun bindClassRepository(
        impl: ClassRepositoryImpl
    ): ClassRepository
}