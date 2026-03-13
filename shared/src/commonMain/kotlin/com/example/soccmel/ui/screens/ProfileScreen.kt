package com.example.soccmel.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.data.RealRepository
import com.example.soccmel.ui.screens.BolognaBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(userId: String? = null, onPollClick: (String) -> Unit, onLogout: () -> Unit) {
    val currentUser by RealRepository.currentUser.collectAsState()
    val isOwnProfile = userId == null || userId == currentUser?.id
    
    val user = if (isOwnProfile) currentUser else remember(userId) { RealRepository.getUserById(userId!!) }
    val myPolls = remember(user) { 
        if (isOwnProfile) RealRepository.getMyPolls() 
        else emptyList() // For simplicity, only show polls on own profile for now
    }

    var showEditDialog by remember { mutableStateOf(false) }

    if (showEditDialog && isOwnProfile) {
        EditProfileDialog(
            currentName = user?.name ?: "",
            currentBio = user?.bio ?: "",
            currentWebsite = user?.website ?: "",
            onDismiss = { showEditDialog = false },
            onSave = { name, bio, website ->
                RealRepository.updateProfile(name = name, bio = bio, website = website)
                showEditDialog = false
            }
        )
    }

    BolognaBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            user?.name ?: "Profilo", 
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ) 
                    },
                    actions = {
                        if (isOwnProfile) {
                            IconButton(onClick = { /* settings */ }) {
                                Icon(Icons.Filled.Settings, contentDescription = "Impostazioni")
                            }
                            IconButton(onClick = {
                                RealRepository.logout()
                                onLogout()
                            }) {
                                Icon(
                                    Icons.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
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
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ProfileHeader(
                                pollsCount = if (isOwnProfile) myPolls.size else 0,
                                metCount = user?.metUserIds?.size ?: 0,
                                citizenshipScore = user?.citizenshipScore ?: 0
                            )
                            Spacer(Modifier.height(16.dp))
                            ProfileInfo(
                                name = user?.name ?: "",
                                email = user?.email ?: "",
                                bio = user?.bio ?: "",
                                website = user?.website ?: "",
                                location = user?.location ?: "",
                                joinDate = user?.joinDate ?: ""
                            )
                            
                            Spacer(Modifier.height(20.dp))
                            
                            if (isOwnProfile) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { showEditDialog = true },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    ) {
                                        Text("Modifica profilo", fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { /* Share Profile */ },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Condividi", fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else {
                                // Action buttons for other users
                                val canChat = remember(user?.id) { 
                                    user?.id?.let { RealRepository.canChatWith(currentUser?.id ?: "", it) } ?: false 
                                }
                                val hasMet = currentUser?.metUserIds?.contains(user?.id) == true
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { if (canChat) { /* Open Chat */ } },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        enabled = canChat,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (canChat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Icon(
                                            if (canChat) Icons.Filled.Send else Icons.Filled.Lock, 
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(if (canChat) "Invia Messaggio" else "Chat bloccata")
                                    }
                                    
                                    if (!hasMet) {
                                        OutlinedButton(
                                            onClick = { 
                                                user?.id?.let { RealRepository.confirmMeeting(it) }
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Conferma Incontro", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                
                                if (!canChat) {
                                    Text(
                                        "Organizza un evento privato o incontrati dal vivo per scrivervi! 🏰",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                } else if (!hasMet) {
                                    Text(
                                        "Siete amici! Potete scrivervi liberamente. 📩",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }

                            val interests = user?.interests ?: emptyList()
                            if (interests.isNotEmpty()) {
                                Spacer(Modifier.height(24.dp))
                                Text(
                                    "INTERESSI",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    interests.forEach { interest ->
                                        InterestChip(label = interest)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )
                    
                    Text(
                        "I TUOI SONDAGGI",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                val metUserIds = user?.metUserIds ?: emptyList()
            if (metUserIds.isNotEmpty()) {
                item {
                    Text(
                        "INCONTRI REALI",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                itemsIndexed(metUserIds) { index, metId ->
                    val metUser = remember(metId) { RealRepository.getUserById(metId) }
                    Surface(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cittadino #${index + 1}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(100.dp)
                            )
                            Text(
                                text = metUser?.name ?: "Utente",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(16.dp)) }
            }

            if (myPolls.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🤔", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Nessuna attività ancora.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            } else {
                items(myPolls) { poll ->
                    PollCard(poll = poll, onClick = { onPollClick(poll.id) })
                }
            }
        }
    }
}
}

@Composable
fun ProfileHeader(pollsCount: Int, metCount: Int, citizenshipScore: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(84.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 40.sp)
                }
            }
            Spacer(Modifier.height(4.dp))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star, 
                        contentDescription = null, 
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        citizenshipScore.toString(), 
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Sondaggi", count = pollsCount)
            StatItem(label = "Incontri", count = metCount)
        }
    }
}

@Composable
fun StatItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            count.toString(), 
            fontWeight = FontWeight.ExtraBold, 
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            label, 
            fontSize = 12.sp, 
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileInfo(name: String, email: String, bio: String, website: String, location: String, joinDate: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            name, 
            fontWeight = FontWeight.Black, 
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            email,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
        if (bio.isNotEmpty()) {
            Text(
                bio, 
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(Modifier.height(4.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (location.isNotEmpty()) {
                InfoTag(icon = Icons.Filled.LocationOn, text = location)
            }
            if (joinDate.isNotEmpty()) {
                InfoTag(icon = Icons.Filled.DateRange, text = "Da $joinDate")
            }
        }
        
        if (website.isNotEmpty()) {
            Text(
                website.replace("http://", "").replace("https://", ""),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun InfoTag(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentBio: String,
    currentWebsite: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var bio by remember { mutableStateOf(currentBio) }
    var website by remember { mutableStateOf(currentWebsite) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifica Profilo", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Visualizzato") },
                    placeholder = { Text("es: Marco Rossi") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    placeholder = { Text("Racconta qualcosa di te...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    label = { Text("Sito Web / Link") },
                    placeholder = { Text("es: instagram.com/tuonome") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, bio, website) }) {
                Text("Salva")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun InterestChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}
