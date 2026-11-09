package com.example.androidlabs.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onDoneClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val currentSettings by viewModel.settings.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Настройки поиска и фильтрации",
            style = MaterialTheme.typography.headlineSmall
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Сортировать по:", style = MaterialTheme.typography.titleMedium)
            val sortOptions = listOf(
                "updated" to "Дате обновления",
                "stars" to "Звездам",
                "forks" to "Форкам"
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                sortOptions.forEach { (value, label) ->
                    FilterChip(
                        selected = currentSettings.sortBy == value,
                        onClick = { viewModel.updateSortBy(value) },
                        label = { Text(label) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Язык программирования:", style = MaterialTheme.typography.titleMedium)
            val languages = listOf("" to "Все", "Kotlin" to "Kotlin", "Java" to "Java", "Python" to "Python", "C" to "C")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                languages.forEach { (langValue, langLabel) ->
                    FilterChip(
                        selected = currentSettings.language == langValue,
                        onClick = { viewModel.updateLanguage(langValue) },
                        label = { Text(langLabel) }
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Минимум звезд: ${currentSettings.minStars} ⭐", style = MaterialTheme.typography.titleMedium)
            val starOptions = listOf(0, 10, 50, 100, 500)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                starOptions.forEach { stars ->
                    FilterChip(
                        selected = currentSettings.minStars == stars,
                        onClick = { viewModel.updateMinStars(stars) },
                        label = { Text(if (stars == 0) "Любое (0)" else "> $stars ⭐") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.resetToDefaults()
                    onDoneClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сбросить")
            }

            Button(
                onClick = {
                    viewModel.saveSettings()
                    onDoneClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Готово")
            }
        }
    }
}