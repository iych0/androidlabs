package com.example.androidlabs.data

import com.example.androidlabs.data.remote.RetrofitClient
import com.example.androidlabs.data.repository.GithubRepositoryImpl
import com.example.androidlabs.domain.repository.GithubRepository
import com.example.androidlabs.domain.usecase.GetUserReposUseCase

object AppContainer {
    val repository: GithubRepository by lazy {
        GithubRepositoryImpl(RetrofitClient.api)
    }
    val getUserReposUseCase by lazy {
        GetUserReposUseCase(repository)
    }
}