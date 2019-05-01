@file:Suppress("RemoveExplicitTypeArguments")

package com.marknkamau.justjavastaff.di

import com.marknkamau.justjavastaff.ColorUtils
import com.marknkamau.justjavastaff.data.local.SettingsRepoImpl
import com.marknkamau.justjavastaff.data.local.SettingsRespository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<SettingsRespository> { SettingsRepoImpl(androidContext()) }
    single<ColorUtils> { ColorUtils(androidContext()) }
}