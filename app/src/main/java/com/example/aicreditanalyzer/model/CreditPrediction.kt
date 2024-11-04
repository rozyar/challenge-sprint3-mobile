package com.example.aicreditanalyzer.model

data class CreditPrediction(
    val id: String = "",
    val amount: String = "",
    val income: String = "",
    val result: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
