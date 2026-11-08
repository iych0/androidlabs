package com.example.androidlabs.presentation.screens.list

import com.example.androidlabs.data.model.GithubRepo
sealed interface ListUiState {
    data object Loading : ListUiState
    data class Success(val repos: List<GithubRepo>) : ListUiState
    data class Error(val message: String) : ListUiState
}