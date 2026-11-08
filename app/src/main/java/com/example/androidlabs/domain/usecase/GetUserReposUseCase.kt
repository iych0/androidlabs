package com.example.androidlabs.domain.usecase

import com.example.androidlabs.domain.model.GithubRepo
import com.example.androidlabs.domain.repository.GithubRepository

class GetUserReposUseCase(
    private val repository: GithubRepository
) {
    suspend operator fun invoke(username: String, sort: String = "updated"): List<GithubRepo> {
        return repository.getUserRepos(username = username, sort = sort)
    }
}