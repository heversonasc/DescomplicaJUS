package com.example.jurisimplificado.data.model

import com.google.gson.annotations.SerializedName

data class GroqMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)