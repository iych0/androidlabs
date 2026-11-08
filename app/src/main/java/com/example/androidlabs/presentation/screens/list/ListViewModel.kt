package com.example.androidlabs.presentation.screens.list

import androidx.lifecycle.ViewModel
import com.example.androidlabs.data.repository.MockGithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadRepos()
    }

    private fun loadRepos() {
        val data = MockGithubRepository.getRepos()
        _uiState.value = ListUiState.Success(repos = data)
    }
}