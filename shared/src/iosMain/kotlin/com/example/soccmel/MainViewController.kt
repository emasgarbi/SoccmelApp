package com.example.soccmel

import androidx.compose.ui.window.ComposeUIViewController
import com.example.soccmel.data.RealRepository

fun MainViewController() = ComposeUIViewController {
    // Il repository su iOS non ha bisogno di context per SharedPreferences (UserDefaults)
    RealRepository.init()
    
    App()
}
