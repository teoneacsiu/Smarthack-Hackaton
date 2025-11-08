package com.hailavirtual.di

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.hailavirtual.core.session.SessionManager
import com.hailavirtual.data.repo.GeminiRepositoryImpl
import com.hailavirtual.domain.repo.GeminiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Providers {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext ctx: Context): SessionManager =
        SessionManager.from(ctx)

    @Provides
    @Singleton
    fun provideGeminiRepository(
        model: GenerativeModel
    ): GeminiRepository = GeminiRepositoryImpl(model)
}