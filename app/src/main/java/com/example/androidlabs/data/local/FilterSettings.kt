package com.example.androidlabs.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

data class FilterSettings(
    val sortBy: String = DEFAULT_SORT,
    val language: String = DEFAULT_LANGUAGE,
    val minStars: Int = DEFAULT_MIN_STARS
) {
    val isNonDefault: Boolean
        get() = sortBy != DEFAULT_SORT || language != DEFAULT_LANGUAGE || minStars != DEFAULT_MIN_STARS

    companion object {
        const val DEFAULT_SORT = "updated"
        const val DEFAULT_LANGUAGE = ""
        const val DEFAULT_MIN_STARS = 0
    }
}

class BadgeCache {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()

    fun updateBadgeState(shouldShow: Boolean) {
        _showBadge.value = shouldShow
    }
}

class SettingsStorage(context: Context, private val badgeCache: BadgeCache) {
    private val prefs = context.getSharedPreferences("github_filter_prefs", Context.MODE_PRIVATE)

    init {
        badgeCache.updateBadgeState(loadSettings().isNonDefault)
    }

    fun loadSettings(): FilterSettings {
        return FilterSettings(
            sortBy = prefs.getString("sort_by", FilterSettings.DEFAULT_SORT) ?: FilterSettings.DEFAULT_SORT,
            language = prefs.getString("language", FilterSettings.DEFAULT_LANGUAGE) ?: FilterSettings.DEFAULT_LANGUAGE,
            minStars = prefs.getInt("min_stars", FilterSettings.DEFAULT_MIN_STARS)
        )
    }

    fun saveSettings(settings: FilterSettings) {
        prefs.edit {
            putString("sort_by", settings.sortBy)
                .putString("language", settings.language)
                .putInt("min_stars", settings.minStars)
        }

        badgeCache.updateBadgeState(settings.isNonDefault)
    }
}