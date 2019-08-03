package com.marknkamau.justjava.testUtils

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.data.local.CartDatabase
import com.marknkamau.justjava.di.presentersModule
import com.marknkamau.justjava.testUtils.mocks.*
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class TestApp : JustJavaApp() {

    companion object {
        val mockPreferencesRepository = MockPreferencesRepository.create()
        val mockNotificationHelper = MockNotificationHelper.create()
        val mockAuthService = MockAuthService.create()
        val mockUserService = MockUserService.create()
        val mockOrderService = MockOrderService.create()
        val mockPaymentsService = MockPaymentsService.create()
        val mockMpesaInteractor = MockMpesaInteractor.create()
    }

    private val context by lazy { InstrumentationRegistry.getInstrumentation().context }

    override fun onCreate() {

        startKoin {
            modules(listOf(mockAppModule, presentersModule, mockAuthModule, mockDatabaseModule, mockMpesaModule))
        }
    }

    private val mockAppModule = module {
        single { mockPreferencesRepository }
        single { Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java).build() }
        single { get<CartDatabase>().cartDao() }
        single { mockNotificationHelper }
        single(named("Main")) { Dispatchers.Unconfined }
    }

    private val mockAuthModule = module {
        single { mockAuthService }
    }

    private val mockDatabaseModule = module {
        single { mockUserService }
        single { mockOrderService }
        single { mockPaymentsService }
    }

    val mockMpesaModule = module {
        single { mockMpesaInteractor }
    }
}