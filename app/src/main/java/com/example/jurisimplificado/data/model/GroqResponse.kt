package com.example.jurisimplificado.data.model

import com.google.gson.annotations.SerializedName

data class GroqResponse(
    @SerializedName("choices") val choices: List<Choice>
)