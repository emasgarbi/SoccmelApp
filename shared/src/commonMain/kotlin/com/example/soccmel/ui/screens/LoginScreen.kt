package com.example.soccmel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soccmel.data.RealRepository
import com.example.soccmel.ui.screens.BolognaBackground

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current

    BolognaBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Soccmel", 
                style = MaterialTheme.typography.displayMedium, 
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-2).sp
            )
            Text(
                "Socialità sana per Bologna", 
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    TabRow(
                        selectedTabIndex = if (isLoginMode) 0 else 1,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary,
                        divider = {}
                    ) {
                        Tab(
                            selected = isLoginMode,
                            onClick = { isLoginMode = true; errorMessage = null },
                            text = { Text("Accedi", fontWeight = FontWeight.Bold) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Tab(
                            selected = !isLoginMode,
                            onClick = { isLoginMode = false; errorMessage = null },
                            text = { Text("Registrati", fontWeight = FontWeight.Bold) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it.lowercase().trim()
                            errorMessage = null 
                        },
                        label = { Text("Indirizzo Email") },
                        placeholder = { Text("es: marco@soccmel.bo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = errorMessage != null && email.isEmpty()
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            errorMessage = null 
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) 
                                Icons.Filled.Visibility 
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = "Mostra password")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    
                    if (errorMessage != null) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            errorMessage!!, 
                            color = MaterialTheme.colorScheme.error, 
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                if (!email.contains("@")) {
                                    errorMessage = "Inserisci un'email valida"
                                } else {
                                    val error = if (isLoginMode) {
                                        RealRepository.login(context, email, password)
                                    } else {
                                        RealRepository.register(context, email, password)
                                    }
                                    
                                    if (error == null) {
                                        onLoginSuccess()
                                    } else {
                                        errorMessage = error
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = email.isNotBlank() && password.isNotBlank()
                    ) {
                        Text(
                            if (isLoginMode) "Accedi" else "Crea Account", 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 16.sp
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            Text(
                if (isLoginMode) 
                    "Bentornato! Inserisci le tue credenziali per accedere." 
                else 
                    "Soccmel è una community reale. Registrati per iniziare a contribuire alla vita di Bologna.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}
