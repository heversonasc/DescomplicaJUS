package com.example.jurisimplificado.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jurisimplificado.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Fundo preto
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = "Logo DescomplicaJus",
            modifier = Modifier.size(width = 300.dp, height = 80.dp)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onTimeout = {})
}