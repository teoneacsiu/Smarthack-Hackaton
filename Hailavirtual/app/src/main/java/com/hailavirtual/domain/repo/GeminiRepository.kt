package com.hailavirtual.domain.repo

import com.hailavirtual.data.model.GeminiResult
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {
    fun generate(prompt: String): Flow<GeminiResult>
}