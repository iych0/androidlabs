package com.example.androidlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidlabs.di.appModule
import com.example.androidlabs.presentation.screens.main.MainScreen
import com.example.androidlabs.profile.profileModule
import com.example.androidlabs.ui.theme.AndroidLabsTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinApplication(
                configuration = koinConfiguration(declaration = {
                    androidContext(this@MainActivity)
                    modules(appModule, profileModule)
                }),
                content = {
                    AndroidLabsTheme {
                        MainScreen()
                    }
                }
            )
        }
    }
}