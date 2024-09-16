package com.example.aicreditanalyzer.model

data class GPTResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

data class Message(
    val role: String,
    val content: String
)