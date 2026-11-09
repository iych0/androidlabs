package com.example.androidlabs.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

data class UserProfile(
    val fullName: String = "Святогор Магомедов",
    val position: String = "Senior JSON/YAML developer",
    val avatarUri: String? = null,
    val resumeUrl: String = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
)

class ProfileStorage(context: Context) {
    private val prefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)

    private val _profileFlow = MutableStateFlow(loadFromPrefs())
    val profileFlow: StateFlow<UserProfile> = _profileFlow.asStateFlow()

    private fun loadFromPrefs(): UserProfile {
        return UserProfile(
            fullName = prefs.getString("full_name", "Святогор Магомедов") ?: "Святогор Магомедов",
            position = prefs.getString("position", "Senior JSON/YAML developer") ?: "",
            avatarUri = prefs.getString("avatar_uri", null),
            resumeUrl = prefs.getString(
                "resume_url",
                "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
            ) ?: ""
        )
    }

    fun saveProfile(profile: UserProfile) {
        prefs.edit {
            putString("full_name", profile.fullName)
                .putString("position", profile.position)
                .putString("avatar_uri", profile.avatarUri)
                .putString("resume_url", profile.resumeUrl)
        }

        _profileFlow.value = profile
    }
}