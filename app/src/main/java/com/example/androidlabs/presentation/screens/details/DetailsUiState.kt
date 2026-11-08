package com.example.androidlabs.presentation.screens.details

import com.example.androidlabs.domain.model.GithubRepo

class sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val repos: List<GithubRepo>) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}
