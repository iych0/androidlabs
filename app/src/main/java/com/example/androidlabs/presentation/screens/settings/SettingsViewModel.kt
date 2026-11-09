package com.example.androidlabs.presentation.screens.settings

import androidx.lifecycle.ViewModel
import com.example.androidlabs.data.local.FilterSettings
import com.example.androidlabs.data.local.SettingsStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    private val _settings = MutableStateFlow(settingsStorage.loadSettings())
    val settings: StateFlow<FilterSettings> = _settings.asStateFlow()

    fun updateSortBy(sort: String) {
        _settings.value = _settings.value.copy(sortBy = sort)
    }

    fun updateLanguage(lang: String) {
        _settings.value = _settings.value.copy(language = lang)
    }

    fun updateMinStars(stars: Int) {
        _settings.value = _settings.value.copy(minStars = stars)
    }

    fun saveSettings() {
        settingsStorage.saveSettings(_settings.value)
    }

    fun resetToDefaults() {
        val defaults = FilterSettings()
        _settings.value = defaults
        settingsStorage.saveSettings(defaults)
    }
}