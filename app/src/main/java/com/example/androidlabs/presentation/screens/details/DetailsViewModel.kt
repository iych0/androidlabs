package com.example.androidlabs.presentation.screens.details

import androidx.lifecycle.ViewModel
import com.example.androidlabs.data.repository.MockGithubRepository
import com.example.androidlabs.data.model.GithubRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsViewModel : ViewModel() {
    private val _repo = MutableStateFlow<GithubRepo?>(null)
    val repo: StateFlow<GithubRepo?> = _repo.asStateFlow()

    fun loadRepo(id: Long) {
        _repo.value = MockGithubRepository.getRepoById(id)
    }
}