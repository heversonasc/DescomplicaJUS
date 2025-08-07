package com.example.jurisimplificado.data.model

import com.google.gson.annotations.SerializedName

data class GroqRequest(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<GroqMessage>
)