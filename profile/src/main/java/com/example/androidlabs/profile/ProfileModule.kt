package com.example.androidlabs.profile

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val profileModule = module {
    single { ProfileStorage(context = androidContext()) }
}
