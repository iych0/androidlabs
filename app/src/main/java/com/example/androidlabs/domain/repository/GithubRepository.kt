package com.example.androidlabs.domain.repository

import com.example.androidlabs.domain.model.GithubRepo
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun getUserRepos(username: String, sort: String): List<GithubRepo>

    suspend fun getRepoById(id: Long): GithubRepo?

    fun getFavoriteRepos(): Flow<List<GithubRepo>>
    fun isRepoFavorite(id: Long): Flow<Boolean>
    suspend fun toggleFavorite(repo: GithubRepo, isCurrentlyFavorite: Boolean)
}