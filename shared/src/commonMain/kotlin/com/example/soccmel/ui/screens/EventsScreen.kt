package com.example.soccmel.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.data.RealRepository
import com.example.soccmel.model.BolognaEvent
import com.example.soccmel.model.EventStatus
import com.example.soccmel.ui.screens.BolognaBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    val events by RealRepository.events.collectAsState()
    val friendRequests by RealRepository.friendRequests.collectAsState()
    val currentUser by RealRepository.currentUser.collectAsState()

    BolognaBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Column {
                            Text("Bologna Eventi", style = MaterialTheme.typography.titleLarge)
                            Text("Cosa succede in città", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (friendRequests.isNotEmpty()) {
                    item {
                        FriendRequestsWidget(requests = friendRequests)
                    }
                }

                items(events) { event ->
                    EventCard(event = event, currentUserId = currentUser?.id)
                }
            }
        }
    }
}

@Composable
fun FriendRequestsWidget(requests: List<com.example.soccmel.model.FriendRequest>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "RICHIESTE DI AMICIZIA",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(8.dp))
            requests.forEach { request ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "@${request.fromUserName} ti ha chiesto l'amicizia",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { RealRepository.respondToRequest(request.id, true) }) {
                        Text("Accetta", fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = { RealRepository.respondToRequest(request.id, false) }) {
                        Text("Rifiuta", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: BolognaEvent, currentUserId: String?) {
    val isOrganizer = currentUserId != null && event.organizerId == currentUserId
    val isParticipant = currentUserId != null && event.participantIds.contains(currentUserId)
    val isCompleted = event.status == EventStatus.COMPLETED

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) 
            else 
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (!event.isFixed && !isCompleted) {
                        Text(
                            text = " • ${event.participantIds.size} PARTECIPANTI",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Surface(
                    color = when {
                        isCompleted -> MaterialTheme.colorScheme.outlineVariant
                        event.isFixed -> MaterialTheme.colorScheme.surfaceVariant
                        event.isPublic -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when {
                            isCompleted -> "COMPLETATO"
                            event.isFixed -> "EVENTO FISSO"
                            event.isPublic -> "PUBBLICO"
                            else -> "TRA AMICI"
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) MaterialTheme.colorScheme.outline else Color.Unspecified
            )
            
            if (event.organizerId != null) {
                val organizer = remember(event.organizerId) { RealRepository.getUserById(event.organizerId) }
                Text(
                    text = "Organizzato da @${organizer?.name ?: "utente"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!isCompleted) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isOrganizer) {
                        Button(
                            onClick = { RealRepository.completeEvent(event.id) },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Concludi", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    } else if (isParticipant) {
                        OutlinedButton(
                            onClick = { RealRepository.leaveEvent(event.id) },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Annulla", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    } else if (!event.isFixed) {
                        Button(
                            onClick = { RealRepository.joinEvent(event.id) },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Partecipa", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    if (event.supportContact != null) {
                        OutlinedButton(
                            onClick = { /* Open contact */ },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Contatta", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Logistical info stacked vertically to handle long text
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCompleted) MaterialTheme.colorScheme.outline else Color.Unspecified
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCompleted) MaterialTheme.colorScheme.outline else Color.Unspecified
                    )
                }
            }
        }
    }
}
