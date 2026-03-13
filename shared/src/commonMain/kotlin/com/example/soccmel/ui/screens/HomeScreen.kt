package com.example.soccmel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.data.RealRepository
import com.example.soccmel.model.Poll
import com.example.soccmel.model.DialectWord
import com.example.soccmel.ui.screens.BolognaBackground
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPollClick: (String) -> Unit, 
    onAddPollClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onScoreClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val polls by RealRepository.polls.collectAsState()
    val currentUser by RealRepository.currentUser.collectAsState()
    val wordOfTheDay by RealRepository.wordOfTheDay.collectAsState()
    
    var showMenu by remember { mutableStateOf(false) }

    BolognaBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Soccmel", 
                            fontWeight = FontWeight.Black, 
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = (-1).sp
                        ) 
                    },
                    navigationIcon = {
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Filled.Settings, contentDescription = "Impostazioni", tint = MaterialTheme.colorScheme.primary)
                            }
                            
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Impostazioni") },
                                    onClick = { 
                                        showMenu = false
                                        onSettingsClick()
                                    },
                                    leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Punteggio Cittadinanza") },
                                    onClick = { 
                                        showMenu = false
                                        onScoreClick()
                                    },
                                    leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Notifiche") },
                                    onClick = { 
                                        showMenu = false
                                        onNotificationsClick()
                                    },
                                    leadingIcon = { Icon(Icons.Filled.Notifications, contentDescription = null) }
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onSearchClick) {
                            Icon(Icons.Filled.Search, contentDescription = "Cerca Utenti", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                // Elegant Welcome Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bentornato, @${currentUser?.name ?: "utente"}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Scopri cosa succede oggi a Bologna",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (currentUser?.name ?: "U").take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Word of the Day Widget
                DialectWordWidget(word = wordOfTheDay)

                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                if (polls.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Nessun sondaggio dagli amici.\nCrea il primo!", 
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(1.dp) // Instagram style divider
                    ) {
                        items(polls) { poll ->
                            PollCard(poll, onClick = { onPollClick(poll.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialectWordWidget(word: DialectWord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LA PAROLA DEL GIORNO",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Dialetto Bolognese",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = word.word,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (word.example != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "\"${word.example}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}
