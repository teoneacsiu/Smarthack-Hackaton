package com.hailavirtual.data.repo

import com.google.firebase.ai.GenerativeModel
import com.hailavirtual.data.model.GeminiResult
import com.hailavirtual.domain.repo.GeminiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRepositoryImpl @Inject constructor(
    private val model: GenerativeModel
) : GeminiRepository {

    override fun generate(prompt: String): Flow<GeminiResult> = flow {
        emit(GeminiResult.Processing)
        try {
            val resp = model.generateContent(prompt)
            emit(GeminiResult.Completed(resp.text.orEmpty()))
        } catch (t: Throwable) {
            emit(GeminiResult.Error(t.message))
        }
    }
}