
package com.example.jurisimplificado.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jurisimplificado.data.model.AuthState
import com.example.jurisimplificado.ui.theme.JuriSimplificadoTheme
import com.example.jurisimplificado.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }
            else -> Unit
        }
    }
    LoginContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = {
            authViewModel.login(email, password)
        },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = onNavigateToForgotPassword,
        isLoading = authState is AuthState.Loading
    )
}

@Composable
private fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Login",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.weight(1f).padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            TextButton(
                onClick = onForgotPasswordClick,
                enabled = !isLoading
            ) {
                Text("Esqueceu a senha?", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Não tem uma conta? ")
                }
                pushStringAnnotation(tag = "CADASTRE_SE", annotation = "CADASTRE_SE")
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                    append("Cadastre-se")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    if (!isLoading) {
                        annotatedText.getStringAnnotations(tag = "CADASTRE_SE", start = offset, end = offset)
                            .firstOrNull()?.let { onNavigateToRegister() }
                    }
                }
            )
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "Login Screen")
@Composable
fun LoginContentPreview() {
    JuriSimplificadoTheme {
        LoginContent(
            email = "exemplo@email.com",
            onEmailChange = {},
            password = "senha",
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {},
            onForgotPasswordClick = {},
            isLoading = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "Login Screen Loading")
@Composable
fun LoginContentLoadingPreview() {
    JuriSimplificadoTheme {
        LoginContent(
            email = "exemplo@email.com",
            onEmailChange = {},
            password = "senha",
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {},
            onForgotPasswordClick = {},
            isLoading = true
        )
    }
}