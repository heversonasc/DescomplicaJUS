package com.example.jurisimplificado.data.network

import com.example.jurisimplificado.data.model.GroqRequest
import com.example.jurisimplificado.data.model.GroqResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GroqApiService {

    @POST("openai/v1/chat/completions")
    suspend fun getChatCompletion(
        @Body requestBody: GroqRequest
    ): GroqResponse

}