package com.example.androidlabs.data.model

data class GithubRepo(
    val id: Long,
    val name: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val updatedAt: String
)