package com.marknkamau.justjava.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.lifecycleScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.utils.toast
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
    private val authRepository: AuthRepository by inject()
    protected open var requiresSignedIn = false

    protected fun handleApiError(resource: Resource.Failure<Any>) {
        if (resource.response.message == "Invalid session-id") {
            lifecycleScope.launch {
                authRepository.signOutLocally()
                toast("You have been signed out")
                if (requiresSignedIn) {
                    goToMainActivityNoStack()
                }
            }
        } else {
            toast(resource.response.message)
        }
    }

    private fun goToMainActivityNoStack() {
        val intent = Intent(this, MainActivity::class.java)
        TaskStackBuilder.create(this)
            .addNextIntentWithParentStack(intent)
            .startActivities()
    }
}
