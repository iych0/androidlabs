package com.example.androidlabs.di

import androidx.room.Room
import com.example.androidlabs.data.local.AppDatabase
import com.example.androidlabs.data.local.BadgeCache
import com.example.androidlabs.data.local.ProfileStorage
import com.example.androidlabs.data.local.SettingsStorage
import com.example.androidlabs.data.remote.RetrofitClient
import com.example.androidlabs.data.repository.GithubRepositoryImpl
import com.example.androidlabs.domain.repository.GithubRepository
import com.example.androidlabs.domain.usecase.GetUserReposUseCase
import com.example.androidlabs.presentation.screens.details.DetailsViewModel
import com.example.androidlabs.presentation.screens.favorites.FavoritesViewModel
import com.example.androidlabs.presentation.screens.list.ListViewModel
import com.example.androidlabs.presentation.screens.profile.ProfileViewModel
import com.example.androidlabs.presentation.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.api }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "github_labs.db"
        ).build()
    }
    single { get<AppDatabase>().favoriteDao() }

    single { BadgeCache() }
    single { SettingsStorage(context = androidContext(), badgeCache = get()) }

    single<GithubRepository> { GithubRepositoryImpl(api = get(), favoriteDao = get()) }
    single { GetUserReposUseCase(repository = get()) }

    single { ProfileStorage(context = androidContext()) }
    viewModel { ProfileViewModel(profileStorage = get()) }

    viewModel { ListViewModel(getUserReposUseCase = get(), settingsStorage = get(), badgeCache = get()) }
    viewModel { SettingsViewModel(settingsStorage = get()) }
    viewModel { DetailsViewModel(repository = get()) }
    viewModel { FavoritesViewModel(repository = get()) }
}