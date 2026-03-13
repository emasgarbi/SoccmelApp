package com.example.soccmel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.soccmel.data.RealRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inizializza il context Android per il modulo shared
        initAndroidContext(this)
        // Inizializza il repository (che ora è in commonMain)
        RealRepository.init()
        
        enableEdgeToEdge()
        setContent {
            App() // Chiamata alla funzione Composable condivisa
        }
    }
}
