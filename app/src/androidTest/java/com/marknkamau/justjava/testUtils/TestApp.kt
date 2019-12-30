package com.marknkamau.justjava.testUtils

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.data.db.AppDatabase
import com.marknkamau.justjava.testUtils.mocks.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TestApp : JustJavaApp() {

    companion object {
        val mockPreferencesRepository = MockPreferencesRepository.create()
        val mockNotificationHelper = MockNotificationHelper.create()
    }

    private val context by lazy { InstrumentationRegistry.getInstrumentation().context }

    override fun onCreate() {

        startKoin {
            modules(listOf(mockAppModule))
        }
    }

    private val mockAppModule = module {
        single { mockPreferencesRepository }
        single { Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build() }
        single { get<AppDatabase>().cartDao() }
        single { mockNotificationHelper }
    }

}