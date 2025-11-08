package com.hailavirtual.data.model

sealed interface GeminiResult {

    // Nimic nu s-a întamplat inca (stare initiala)
    data object Waiting : GeminiResult

    // Cererea este in curs de procesare
    data object Processing : GeminiResult

    // Raspuns primit cu succes
    data class Completed(
        val text: String
    ) : GeminiResult

    // A aparut o eroare
    data class Error(
        val message: String? = null,
        val throwable: Throwable? = null
    ) : GeminiResult
}