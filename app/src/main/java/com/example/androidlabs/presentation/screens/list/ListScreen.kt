package com.example.androidlabs.presentation.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import com.example.androidlabs.presentation.components.repocard.RepoCard

@Composable
fun RepoListScreen(
    onRepoClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val username by viewModel.usernameQuery.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRepos()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        UserSearchBar(
            username = username,
            onValueChange = { viewModel.onUsernameChange(it) },
            onSearchClick = { viewModel.loadRepos() },
            onSettingsClick = onSettingsClick
        )

        when (val currentState = state) {
            is ListUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ListUiState.Error -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { viewModel.loadRepos() }) {
                    Text("Повторить")
                }
            }
            is ListUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = currentState.repos,
                        key = { it.id }
                    ) { repo ->
                        RepoCard(
                            repo = repo,
                            onRepoClick = onRepoClick
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun UserSearchBar(
    username: String,
    onSearchClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { onValueChange(it) },
            label = { Text("GitHub логин") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { onSearchClick() }) {
            Text("Найти")
        }
        SettingsButton(
            onClick = onSettingsClick
        )
    }
}

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    showBadge: Boolean = false,
) {
    IconButton(onClick = onClick) {
        BadgedBox(
            badge = {
                if (showBadge) {
                    Badge(
                        containerColor = Color(0xFFF57C00), // оранжевый
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Настройки фильтрации"
            )
        }
    }
}