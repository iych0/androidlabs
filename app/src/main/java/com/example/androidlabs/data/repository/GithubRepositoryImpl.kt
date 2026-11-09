package com.example.androidlabs.data.repository

import com.example.androidlabs.data.local.FavoriteRepoDao
import com.example.androidlabs.data.local.toDomain
import com.example.androidlabs.data.local.toEntity
import com.example.androidlabs.data.remote.GithubApi
import com.example.androidlabs.data.remote.toDomain
import com.example.androidlabs.domain.model.GithubRepo
import com.example.androidlabs.domain.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GithubRepositoryImpl(
    private val api: GithubApi,
    private val favoriteDao: FavoriteRepoDao
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
            ?: favoriteDao.getAllFavorites().firstOrNull()?.find { it.id == id }?.toDomain()
    }

    override fun getFavoriteRepos(): Flow<List<GithubRepo>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isRepoFavorite(id: Long): Flow<Boolean> {
        return favoriteDao.isFavorite(id)
    }

    override suspend fun toggleFavorite(repo: GithubRepo, isCurrentlyFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            if (isCurrentlyFavorite) {
                favoriteDao.deleteFavoriteById(repo.id)
            } else {
                favoriteDao.insertFavorite(repo.toEntity())
            }
        }
    }
}