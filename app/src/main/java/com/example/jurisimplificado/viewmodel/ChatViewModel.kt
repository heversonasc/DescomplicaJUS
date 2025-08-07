import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jurisimplificado.data.model.GroqMessage
import com.example.jurisimplificado.data.model.GroqRequest
import com.example.jurisimplificado.ui.chat.ChatMessage
import com.example.jurisimplificado.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val systemPrompt = GroqMessage(
        role = "system",
        content = "Você é o 'DescomplicaJus', um assistente especialista em direito brasileiro. Sua missão é tirar dúvidas jurídicas de forma extremamente simples, clara e objetiva. Evite jargões e termos técnicos (o famoso 'juridiquês'). Fale como se estivesse conversando com um amigo leigo, usando um português brasileiro coloquial e limpo. Seja direto e didático."
    )

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    fun sendMessage(userMessage: String) {
        val userChatMessage = ChatMessage(text = userMessage, isUser = true)
        _messages.update { currentMessages -> currentMessages + userChatMessage }

        viewModelScope.launch {
            val loadingMessage = ChatMessage(text = "Digitando...", isUser = false, isLoading = true)
            _messages.update { currentMessages -> currentMessages + loadingMessage }

            val conversationHistory = mutableListOf<GroqMessage>()
            conversationHistory.add(systemPrompt)
            _messages.value.filterNot { it.isLoading }.forEach { msg ->
                conversationHistory.add(
                    GroqMessage(
                        role = if (msg.isUser) "user" else "assistant",
                        content = msg.text
                    )
                )
            }

            try {
                val requestBody = GroqRequest(
                    model = "llama3-8b-8192",
                    messages = conversationHistory
                )


                val response = apiService.getChatCompletion(requestBody)

                val reply = response.choices.firstOrNull()?.message?.content?.trim() ?: "Desculpe, não consegui pensar em uma resposta."
                val aiChatMessage = ChatMessage(text = reply, isUser = false)

                _messages.update { currentMessages ->
                    currentMessages.dropLast(1) + aiChatMessage
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Falha na chamada da API com Retrofit", e)
                val errorMessage = ChatMessage(text = "Ops! Ocorreu um erro: ${e.message}", isUser = false)
                _messages.update { currentMessages ->
                    currentMessages.dropLast(1) + errorMessage
                }
            }
        }
    }
}