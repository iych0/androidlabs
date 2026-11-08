package com.example.androidlabs.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.data.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val getUserReposUseCase = AppContainer.getUserReposUseCase
    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private val _usernameQuery = MutableStateFlow("iych0")
    val usernameQuery: StateFlow<String> = _usernameQuery.asStateFlow()

    init {
        loadRepos()
    }

    fun onUsernameChange(newUsername: String) {
        _usernameQuery.value = newUsername
    }

    fun loadRepos() {
        val currentUsername = _usernameQuery.value.trim()
        if (currentUsername.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = ListUiState.Loading

            try {
                val repos = getUserReposUseCase(username = currentUsername, sort = "stars")
                _uiState.value = ListUiState.Success(repos)
            } catch (e: Exception) {
                _uiState.value = ListUiState.Error(
                    message = "Ошибка загрузки: ${e.localizedMessage ?: "ничем не могу помочь"}"
                )
            }
        }
    }
}