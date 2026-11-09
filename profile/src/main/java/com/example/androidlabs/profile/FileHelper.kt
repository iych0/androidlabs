package com.example.androidlabs.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileHelper {
    fun createCameraImageUri(context: Context): Uri {
        val file = File(context.filesDir, "avatar_camera_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun copyGalleryImageToInternalStorage(context: Context, sourceUri: Uri): String? {
        return try {
            val destFile = File(context.filesDir, "avatar_gallery_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(destFile).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun downloadResumePdf(context: Context, fileUrl: String): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext Result.failure(Exception("HTTP ${connection.responseCode}"))
                }

                val pdfFile = File(context.filesDir, "downloaded_resume.pdf")
                connection.inputStream.use { input ->
                    FileOutputStream(pdfFile).use { output ->
                        input.copyTo(output)
                    }
                }
                Result.success(pdfFile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun openPdfInViewer(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(Intent.createChooser(intent, "Открыть резюме с помощью"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "На устройстве нет приложения для просмотра PDF",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}