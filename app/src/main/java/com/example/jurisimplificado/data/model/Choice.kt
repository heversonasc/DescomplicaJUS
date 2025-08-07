package com.example.jurisimplificado.data.model

import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("message") val message: GroqMessage
)