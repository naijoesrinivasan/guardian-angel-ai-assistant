package com.example.myapplication


data class GuardianData(val text: String, val type: DataType)
data class PromptRequest(
    val prompt: String
)
enum class DataType {
    REQUEST, RESPONSE
}