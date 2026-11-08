package com.example.androidlabs.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.data.AppContainer
import com.example.androidlabs.domain.model.GithubRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val repository = AppContainer.repository;
    private val _repo = MutableStateFlow<GithubRepo?>(null)
    val repo: StateFlow<GithubRepo?> = _repo.asStateFlow()

    fun loadRepo(id: Long) {
        viewModelScope.launch {
            _repo.value = repository.getRepoById(id)
        }
    }
}