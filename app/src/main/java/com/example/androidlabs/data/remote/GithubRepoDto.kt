package com.example.androidlabs.data.remote

import com.example.androidlabs.domain.model.GithubRepo
import com.google.gson.annotations.SerializedName

// dto с эндпоинта /search/repositories
data class GithubSearchResponseDto(
    @SerializedName("items") val items: List<GithubRepoDto>
)

data class GithubRepoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: OwnerDto,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("forks_count") val forks: Int,
    @SerializedName("language") val language: String?,
    @SerializedName("updated_at") val updatedAt: String
)

data class OwnerDto(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)

fun GithubRepoDto.toDomain(): GithubRepo {
    return GithubRepo(
        id = this.id,
        name = this.name,
        ownerLogin = this.owner.login,
        ownerAvatarUrl = this.owner.avatarUrl,
        description = this.description,
        stars = this.stars,
        forks = this.forks,
        language = this.language,
        updatedAt = this.updatedAt.take(10) // YYYY-MM-DD
    )
}