package com.example.jurisimplificado.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jurisimplificado.data.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Email e senha não podem estar vazios.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        val errorMsg = task.exception?.message ?: "Ocorreu um erro desconhecido."
                        _authState.value = AuthState.Error(errorMsg)
                    }
                }
        }
    }

    fun register(email: String, pass: String, name: String) {
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            _authState.value = AuthState.Error("Todos os campos são obrigatórios.")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("A senha deve ter no mínimo 6 caracteres.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        val errorMsg = task.exception?.message ?: "Não foi possível registrar."
                        _authState.value = AuthState.Error(errorMsg)
                    }
                }
        }

    }
    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error("O campo de e-mail não pode estar vazio.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        val errorMsg = task.exception?.message ?: "Não foi possível enviar o e-mail de recuperação."
                        _authState.value = AuthState.Error(errorMsg)
                    }
                }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        auth.signOut()
    }
}