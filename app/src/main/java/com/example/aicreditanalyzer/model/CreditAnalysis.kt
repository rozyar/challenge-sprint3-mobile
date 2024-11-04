package com.example.aicreditanalyzer.model

data class CreditAnalysis(
    val id: String = "",
    val score: String = "",
    val result: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

