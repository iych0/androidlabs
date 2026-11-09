package com.example.androidlabs.presentation.screens.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlabs.data.local.FileHelper
import com.example.androidlabs.data.local.ProfileStorage
import com.example.androidlabs.data.local.UserProfile
import com.example.androidlabs.presentation.notifications.ClassAlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileStorage: ProfileStorage
) : ViewModel() {

    val profile: StateFlow<UserProfile> = profileStorage.profileFlow

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val timeRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")

    fun isValidTime(time: String): Boolean {
        return timeRegex.matches(time.trim())
    }

    fun saveProfile(
        context: Context,
        fullName: String,
        position: String,
        resumeUrl: String,
        avatarUri: String?,
        favoriteClassTime: String,
    ) {
        val trimmedName = fullName.trim()
        val trimmedTime = favoriteClassTime.trim()
        profileStorage.saveProfile(
            UserProfile(
                fullName = fullName.trim(),
                position = position.trim(),
                resumeUrl = resumeUrl.trim(),
                avatarUri = avatarUri
            )
        )

        ClassAlarmScheduler.scheduleClassReminder(
            context = context,
            timeString = trimmedTime,
            userName = trimmedName
        )
    }

    fun downloadAndOpenResume(context: Context, url: String) {
        if (url.isBlank()) {
            Toast.makeText(context, "Ссылка на резюме пуста", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            _isDownloading.value = true
            val result = FileHelper.downloadResumePdf(context, url)
            _isDownloading.value = false

            result.onSuccess { file ->
                Toast.makeText(context, "Резюме скачано (${file.length() / 1024} КБ)", Toast.LENGTH_SHORT).show()
                FileHelper.openPdfInViewer(context, file)
            }.onFailure { error ->
                Toast.makeText(
                    context,
                    "Ошибка скачивания: ${error.localizedMessage ?: "проверьте ссылку"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}