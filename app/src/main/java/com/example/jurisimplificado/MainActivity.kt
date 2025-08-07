// File: com/example/jurisimplificado/MainActivity.kt

package com.example.jurisimplificado

import ChatScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jurisimplificado.ui.AppNavigation
import com.example.jurisimplificado.ui.theme.JuriSimplificadoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            JuriSimplificadoTheme {
                AppNavigation()
            }
        }
    }
}