package com.example.androidlabs.presentation.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.androidlabs.data.local.FileHelper
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val savedProfile by viewModel.profile.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var fullName by remember(savedProfile) { mutableStateOf(savedProfile.fullName) }
    var position by remember(savedProfile) { mutableStateOf(savedProfile.position) }
    var resumeUrl by remember(savedProfile) { mutableStateOf(savedProfile.resumeUrl) }
    var avatarUri by remember(savedProfile) { mutableStateOf(savedProfile.avatarUri) }
    var favoriteClassTime by remember(savedProfile) { mutableStateOf(savedProfile.favoriteClassTime) }

    val isTimeValid = viewModel.isValidTime(favoriteClassTime)
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val performSaveAndExit = {
        viewModel.saveProfile(
            context = context,
            fullName = fullName,
            position = position,
            resumeUrl = resumeUrl,
            avatarUri = avatarUri,
            favoriteClassTime = favoriteClassTime
        )
        onBackClick()
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            performSaveAndExit()
        } else {
            Toast.makeText(
                context,
                "Разрешите уведомления, чтобы получить напоминание о паре",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val onSaveClick = {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            performSaveAndExit()
        } else {
            notificationPermissionLauncher.launch(permission)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            FileHelper.copyGalleryImageToInternalStorage(context, uri)?.let { avatarUri = it }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            avatarUri = tempCameraUri.toString()
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) galleryLauncher.launch("image/*")
        else Toast.makeText(context, "Нет разрешения на чтение фото", Toast.LENGTH_SHORT).show()
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = FileHelper.createCameraImageUri(context)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Нет разрешения на камеру", Toast.LENGTH_SHORT).show()
        }
    }

    if (showTimePickerDialog) {
        val calendar = Calendar.getInstance()
        val initialHour = favoriteClassTime.split(":").getOrNull(0)?.toIntOrNull()
            ?: calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = favoriteClassTime.split(":").getOrNull(1)?.toIntOrNull()
            ?: calendar.get(Calendar.MINUTE)

        val timePickerState = rememberTimePickerState(
            initialHour = initialHour.coerceIn(0, 23),
            initialMinute = initialMinute.coerceIn(0, 59),
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            title = { Text("Выберите время начала пары") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        favoriteClassTime = String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        showTimePickerDialog = false
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Выбрать аватарку") },
            text = { Text("Откуда следует загрузить новое фото профиля?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            val uri = FileHelper.createCameraImageUri(context)
                            tempCameraUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    }
                ) { Text("Сделать фото") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        val permission =
                            Manifest.permission.READ_MEDIA_IMAGES
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            galleryLauncher.launch("image/*")
                        } else {
                            galleryPermissionLauncher.launch(permission)
                        }
                    }
                ) { Text("Из галереи") }
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Отмена")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSaveClick,
                        enabled = isTimeValid
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Готово")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = "Аватар",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Text(
                text = "Нажмите на фото, чтобы изменить",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = position,
                onValueChange = { position = it },
                label = { Text("Должность / Специализация") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (PDF)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = favoriteClassTime,
                onValueChange = { favoriteClassTime = it },
                label = { Text("Время любимой пары (HH:mm)") },
                placeholder = { Text("Например, 14:30") },
                singleLine = true,
                isError = !isTimeValid,
                supportingText = {
                    if (!isTimeValid) {
                        Text(
                            text = "Введите корректное время в формате HH:mm (от 00:00 до 23:59)",
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text("В это время придет push-уведомление")
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { showTimePickerDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Выбрать время на часах"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSaveClick,
                enabled = isTimeValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}