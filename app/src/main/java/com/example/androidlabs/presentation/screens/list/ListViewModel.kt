package com.example.androidlabs.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.data.local.BadgeCache
import com.example.androidlabs.data.local.SettingsStorage
import com.example.androidlabs.domain.usecase.GetUserReposUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val getUserReposUseCase: GetUserReposUseCase,
    private val settingsStorage: SettingsStorage,
    private val badgeCache: BadgeCache
) : ViewModel() {
    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private val _usernameQuery = MutableStateFlow("iych0")
    val usernameQuery: StateFlow<String> = _usernameQuery.asStateFlow()

    val showBadge: StateFlow<Boolean> = badgeCache.showBadge

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
                val filters = settingsStorage.loadSettings()

                val rawRepos = getUserReposUseCase(
                    username = currentUsername,
                    sort = filters.sortBy
                )

                val filteredRepos = rawRepos.filter { repo ->
                    val matchesLang = filters.language.isEmpty() ||
                            repo.language.equals(filters.language, ignoreCase = true)
                    val matchesStars = repo.stars >= filters.minStars
                    matchesLang && matchesStars
                }

                _uiState.value = ListUiState.Success(filteredRepos)
            } catch (e: Exception) {
                _uiState.value = ListUiState.Error(
                    message = "Ошибка загрузки: ${e.localizedMessage ?: "Проверьте сеть"}"
                )
            }
        }
    }
}