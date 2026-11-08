package com.example.androidlabs.data.repository

import com.example.androidlabs.data.remote.GithubApi
import com.example.androidlabs.data.remote.toDomain
import com.example.androidlabs.domain.model.GithubRepo
import com.example.androidlabs.domain.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class GithubRepositoryImpl(
    private val api: GithubApi
) : GithubRepository {

    private val _cachedRepos = MutableStateFlow<List<GithubRepo>>(emptyList())
    val cachedRepos: Flow<List<GithubRepo>> = _cachedRepos.asStateFlow()

    override suspend fun getUserRepos(username: String, sort: String): List<GithubRepo> {
        return withContext(Dispatchers.IO) {
            val dtoList = api.getUserRepos(username = username, sort = sort)
            val domainList = dtoList.map { it.toDomain() }

            _cachedRepos.value = domainList
            domainList
        }
    }

    override suspend fun getRepoById(id: Long): GithubRepo? {
        return _cachedRepos.value.find { it.id == id }
    }
}