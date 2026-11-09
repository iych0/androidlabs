package com.example.androidlabs.presentation.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.domain.model.GithubRepo
import com.example.androidlabs.domain.repository.GithubRepository
import com.example.androidlabs.presentation.components.repocard.RepoCard
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.androidx.compose.koinViewModel

class FavoritesViewModel(
    repository: GithubRepository
) : ViewModel() {
    val favorites: StateFlow<List<GithubRepo>> = repository.getFavoriteRepos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@Composable
fun FavoritesScreen(
    onRepoClick: (Long) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val favoriteRepos by viewModel.favorites.collectAsStateWithLifecycle()

    if (favoriteRepos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "В избранном пока пусто ★",
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = favoriteRepos, key = { it.id }) { repo ->
                RepoCard(repo = repo, onRepoClick = onRepoClick)
            }
        }
    }
}