package com.example.androidlabs.data.repository

import com.example.androidlabs.data.model.GithubRepo

object MockGithubRepository {
    private val repos = listOf(
        GithubRepo(
            id = 1L,
            name = "kotlin",
            ownerLogin = "JetBrains",
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/878437",
            description = "The Kotlin Programming Language.",
            stars = 49200,
            forks = 5700,
            language = "Kotlin",
            updatedAt = "2026-10-25"
        ),
        GithubRepo(
            id = 2L,
            name = "retrofit",
            ownerLogin = "square",
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/82592",
            description = "A type-safe HTTP client for Android and the JVM",
            stars = 43100,
            forks = 7300,
            language = "Java",
            updatedAt = "2026-11-01"
        ),
        GithubRepo(
            id = 3L,
            name = "compose-samples",
            ownerLogin = "android",
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/32689599",
            description = "Official Jetpack Compose samples.",
            stars = 19800,
            forks = 4800,
            language = "Kotlin",
            updatedAt = "2026-11-02"
        )
    )

    fun getRepos(): List<GithubRepo> = repos

    fun getRepoById(id: Long): GithubRepo? {
        return repos.find { it.id == id }
    }
}