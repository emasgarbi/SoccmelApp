package com.example.soccmel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soccmel.data.RealRepository
import com.example.soccmel.ui.screens.*
import com.example.soccmel.ui.theme.SoccmelTheme

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Activities : Screen("activities_menu", Icons.Filled.List, "Attività")
    object Events : Screen("events", Icons.Filled.DateRange, "Eventi")
    object Profile : Screen("profile", Icons.Filled.Person, "Profilo")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    SoccmelTheme {
        AppMain()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMain() {
    val navController = rememberNavController()
    val currentUser by RealRepository.currentUser.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Check if we are in a screen that should show the bottom bar
    val showBottomBar = currentUser != null && currentDestination?.route !in listOf("login")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(Screen.Home, Screen.Activities, Screen.Events, Screen.Profile)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().route!!) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (currentUser == null) "login" else "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("home") {
                HomeScreen(
                    onPollClick = { pollId -> navController.navigate("poll/$pollId") },
                    onAddPollClick = { navController.navigate("activities_menu") },
                    onSearchClick = { navController.navigate("search_users") },
                    onSettingsClick = { navController.navigate("settings") },
                    onScoreClick = { navController.navigate("citizenship_score") },
                    onNotificationsClick = { navController.navigate("notifications") }
                )
            }
            composable("notifications") {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }
            composable("activities_menu") {
                ActivitiesMenuScreen(
                    onNewEventClick = { navController.navigate("create_event") },
                    onNewPollClick = { navController.navigate("create_poll") },
                    onPollClick = { pollId -> navController.navigate("poll/$pollId") }
                )
            }
            composable("settings") {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
            composable("citizenship_score") {
                CitizenshipScoreScreen(onBack = { navController.popBackStack() })
            }
            composable("events") {
                EventsScreen()
            }
            composable("search_users") {
                SearchUsersScreen(
                    onUserClick = { userId -> navController.navigate("profile/$userId") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("create_poll") {
                CreatePollScreen(
                    onPollCreated = { navController.navigate("home") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("create_event") {
                CreateEventScreen(
                    onEventCreated = { navController.navigate("events") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onPollClick = { pollId -> navController.navigate("poll/$pollId") },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("profile/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                ProfileScreen(
                    userId = userId,
                    onPollClick = { pollId -> navController.navigate("poll/$pollId") },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("poll/{pollId}") { backStackEntry ->
                val pollId = backStackEntry.arguments?.getString("pollId") ?: return@composable
                PollDetailScreen(pollId = pollId, onBack = { navController.popBackStack() })
            }
        }
    }
}
