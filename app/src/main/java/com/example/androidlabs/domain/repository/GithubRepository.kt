package com.example.androidlabs.domain.repository

import com.example.androidlabs.domain.model.GithubRepo

interface GithubRepository {
    suspend fun getUserRepos(username: String, sort: String): List<GithubRepo>

    suspend fun getRepoById(id: Long): GithubRepo?
}