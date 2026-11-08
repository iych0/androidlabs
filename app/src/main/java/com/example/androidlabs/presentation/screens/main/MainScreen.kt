package com.example.androidlabs.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidlabs.presentation.screens.details.DetailsViewModel
import com.example.androidlabs.presentation.screens.details.RepoDetailsScreen
import com.example.androidlabs.presentation.screens.list.RepoListScreen

enum class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    REPOS("repos_tab", "Репозитории", Icons.Default.Home),
    FAVORITES("favorites_tab", "Избранное", Icons.Default.Favorite),
    PROFILE("profile_tab", "Профиль", Icons.Default.Person)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) },
                        label = { Text(text = tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomTab.REPOS.route,
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)
        ) {
            composable(route = BottomTab.REPOS.route) {
                RepoListScreen(
                    onRepoClick = { repoId ->
                        navController.navigate("details/$repoId")
                    }
                )
            }

            composable(
                route = "details/{repoId}",
                arguments = listOf(navArgument("repoId") { type = NavType.LongType })
            ) { backStackEntry ->
                val repoId = backStackEntry.arguments?.getLong("repoId") ?: return@composable

                val detailsViewModel: DetailsViewModel = viewModel()
                val repo by detailsViewModel.repo.collectAsStateWithLifecycle()

                LaunchedEffect(repoId) {
                    detailsViewModel.loadRepo(repoId)
                }

                repo?.let { loadedRepo ->
                    RepoDetailsScreen(
                        repo = loadedRepo,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable(route = BottomTab.FAVORITES.route) {
                PlaceholderScreen(title = "Раздел «Избранное»")
            }

            composable(route = BottomTab.PROFILE.route) {
                PlaceholderScreen(title = "Раздел «Профиль»")
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}