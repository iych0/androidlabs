package com.example.androidlabs.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.domain.model.GithubRepo
import com.example.androidlabs.domain.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _repo = MutableStateFlow<GithubRepo?>(null)
    val repo: StateFlow<GithubRepo?> = _repo.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadRepo(id: Long) {
        viewModelScope.launch {
            _repo.value = repository.getRepoById(id)
        }
        viewModelScope.launch {
            repository.isRepoFavorite(id).collect { fav ->
                _isFavorite.value = fav
            }
        }
    }

    fun toggleFavorite() {
        val currentRepo = _repo.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(currentRepo, _isFavorite.value)
        }
    }
}