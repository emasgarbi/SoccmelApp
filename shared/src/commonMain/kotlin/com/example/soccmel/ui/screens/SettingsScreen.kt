package com.example.soccmel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Impostazioni") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            ListItem(
                headlineContent = { Text("Account") },
                supportingContent = { Text("Gestisci il tuo profilo e privacy") },
                leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Divider()
            ListItem(
                headlineContent = { Text("Notifiche") },
                supportingContent = { Text("Configura avvisi e messaggi") },
                leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                trailingContent = { Switch(checked = true, onCheckedChange = {}) }
            )
            Divider()
            ListItem(
                headlineContent = { Text("Sicurezza") },
                supportingContent = { Text("Password e autenticazione") },
                leadingContent = { Icon(Icons.Default.Lock, contentDescription = null) }
            )
            Divider()
            ListItem(
                headlineContent = { Text("Informazioni") },
                supportingContent = { Text("Versione 1.0.0 - Soccmel Bologna") },
                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
            )
        }
    }
}
