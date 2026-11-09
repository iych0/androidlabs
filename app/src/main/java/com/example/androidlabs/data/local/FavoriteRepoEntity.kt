package com.example.androidlabs.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidlabs.domain.model.GithubRepo

@Entity(tableName = "favorite_repos")
data class FavoriteRepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val updatedAt: String
)

fun GithubRepo.toEntity() = FavoriteRepoEntity(
    id, name, ownerLogin, ownerAvatarUrl, description, stars, forks, language, updatedAt
)

fun FavoriteRepoEntity.toDomain() = GithubRepo(
    id, name, ownerLogin, ownerAvatarUrl, description, stars, forks, language, updatedAt
)