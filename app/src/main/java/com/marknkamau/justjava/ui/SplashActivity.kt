package com.marknkamau.justjava.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SplashActivity : AppCompatActivity() {
    private val preferencesRepository: PreferencesRepository by inject()
    private val usersRepository: UsersRepository by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferencesRepository.isSignedIn) {
            coroutineScope.launch {
                updateFcmToken()
                updateLocalUser()
                proceedToMainActivity()
            }
        } else {
            proceedToMainActivity()
        }
    }

    private suspend fun updateFcmToken() {
        val idResult = FirebaseInstanceId.getInstance().instanceId.await()
        usersRepository.updateFcmToken(idResult.token)
    }

    private suspend fun updateLocalUser() {
        usersRepository.getCurrentUser().collect {  }
    }

    private fun proceedToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}