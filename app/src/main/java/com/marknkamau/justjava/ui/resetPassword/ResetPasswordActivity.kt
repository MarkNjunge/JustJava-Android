package com.marknkamau.justjava.ui.resetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.TaskStackBuilder
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivityResetPasswordBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordActivity : BaseActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var token: String
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.data?.getQueryParameter("token")?.let { token ->
            this.token = token
        } ?: showError()

        binding.tilnewPassword.resetErrorOnChange(binding.etNewPassword)

        binding.btnChangePassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun showError() {
        binding.layoutResetPassword.visibility = View.GONE
        binding.layoutResetPasswordError.visibility = View.VISIBLE
    }

    private fun resetPassword() {
        val newPassword = binding.etNewPassword.text.toString().trim()
        if (newPassword.isEmpty()) {
            binding.tilnewPassword.error = getString(R.string.required)
            return
        }

        uiScope.launch {
            binding.pbLoading.visibility = View.VISIBLE
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
            binding.pbLoading.visibility = View.GONE
        }
    }
}
