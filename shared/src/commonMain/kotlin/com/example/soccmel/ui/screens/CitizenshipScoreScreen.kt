package com.example.soccmel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.data.RealRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenshipScoreScreen(onBack: () -> Unit) {
    val currentUser by RealRepository.currentUser.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Punteggio Cittadinanza") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("🦁", fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Il tuo Punteggio: ${currentUser?.citizenshipScore ?: 0}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "Sei un cittadino esemplare!",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(32.dp))
            }
            
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Come guadagnare punti?", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("• Concludi un evento (+30)")
                        Text("• Incontra un nuovo amico (+50)")
                        Text("• Crea un sondaggio (+5)")
                        Text("• Vota o commenta (+1/+2)")
                    }
                }
            }
        }
    }
}
