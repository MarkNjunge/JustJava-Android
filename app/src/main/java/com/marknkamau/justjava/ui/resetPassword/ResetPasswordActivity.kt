package com.marknkamau.justjava.ui.resetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.TaskStackBuilder
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ResetPasswordActivity : BaseActivity() {

    private val authRepository: AuthRepository by inject()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        intent.data?.getQueryParameter("token")?.let { token ->
            this.token = token
        } ?: showError()

        tilnewPassword.resetErrorOnChange(etNewPassword)

        btnChangePassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun showError() {
        layoutResetPassword.visibility = View.GONE
        layoutResetPasswordError.visibility = View.VISIBLE
    }

    private fun resetPassword() {
        val newPassword = etNewPassword.text.toString().trim()
        if (newPassword.isEmpty()) {
            tilnewPassword.error = getString(R.string.required)
            return
        }

        uiScope.launch {
            pbLoading.visibility = View.VISIBLE
            when (val resource = authRepository.resetPassword(token, newPassword)) {
                is Resource.Success -> {
                    toast("Your password has been changed")
                    val intent = Intent(this@ResetPasswordActivity, SignInActivity::class.java)
                    TaskStackBuilder.create(this@ResetPasswordActivity)
                        .addNextIntentWithParentStack(Intent(this@ResetPasswordActivity, MainActivity::class.java))
                        .addNextIntentWithParentStack(intent)
                        .startActivities()
                }
                is Resource.Failure -> handleApiError(resource)
            }
            pbLoading.visibility = View.GONE
        }
    }
}
