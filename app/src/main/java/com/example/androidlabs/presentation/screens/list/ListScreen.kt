package com.example.androidlabs.presentation.screens.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidlabs.presentation.components.repocard.RepoCard

@Composable
fun RepoListScreen(
    onRepoClick: (Long) -> Unit,
    viewModel: ListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is ListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ListUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message)
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