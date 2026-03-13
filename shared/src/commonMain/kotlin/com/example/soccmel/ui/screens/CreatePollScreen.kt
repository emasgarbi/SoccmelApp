package com.example.soccmel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.soccmel.data.RealRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePollScreen(onPollCreated: () -> Unit, onBack: () -> Unit) {
    var question by remember { mutableStateOf("") }
    var newOption by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nuovo Sondaggio") })
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Qual è il piano?") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newOption,
                    onValueChange = { newOption = it },
                    label = { Text("Nuova Opzione") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    if (newOption.isNotBlank()) {
                        options = options + newOption
                        newOption = ""
                    }
                }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Aggiungi")
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(options) { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(option, modifier = Modifier.weight(1f))
                        IconButton(onClick = { options = options - option }) {
                            Icon(Icons.Filled.Delete, "Rimuovi")
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "💡 Suggerimento: Fai domande che stimolino la conversazione per guadagnare più punti cittadinanza!",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (question.isNotBlank() && options.isNotEmpty()) {
                        RealRepository.createPoll(question, options)
                        onPollCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = question.isNotBlank() && options.isNotEmpty()
            ) {
                Text("Pubblica")
            }
        }
    }
}
