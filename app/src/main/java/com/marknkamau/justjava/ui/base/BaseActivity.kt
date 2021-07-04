package com.marknkamau.justjava.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.lifecycleScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    @Inject lateinit var authRepository: AuthRepository
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
