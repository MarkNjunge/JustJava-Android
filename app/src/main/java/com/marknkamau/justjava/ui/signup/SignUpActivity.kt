package com.marknkamau.justjava.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivitySignUpBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class SignUpActivity : BaseActivity() {

    private val signUpViewModel: SignUpViewModel by viewModel()
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLoading()

        binding.tilEmail.resetErrorOnChange(binding.etEmail)
        binding.tilPassword.resetErrorOnChange(binding.etPassword)
        binding.tilFirstName.resetErrorOnChange(binding.etFirstName)
        binding.tilLastName.resetErrorOnChange(binding.etLastName)

        binding.btnSignup.setOnClickListener {
            if (isValid()) {
                hideKeyboard()
                signUp()
            }
        }
        binding.llSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun observeLoading() {
        signUpViewModel.loading.observe(this, Observer { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun signUp() {
        signUpViewModel.signUp(
            binding.etFirstName.trimmedText,
            binding.etLastName.trimmedText,
            binding.etEmail.trimmedText,
            binding.etPassword.trimmedText
        ).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> finish()
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun isValid(): Boolean {
        var isValid = true

        val justJavaEmailPattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val justJavaEmailMatcher = justJavaEmailPattern.matcher(binding.etEmail.trimmedText)

        if (binding.etEmail.trimmedText.isEmpty()) {
            binding.tilEmail.error = getString(R.string.required)
            isValid = false
        } else if (justJavaEmailMatcher.matches()) {
            binding.tilEmail.error = "Can not use @justjava.com"
            isValid = false
        } else if (!binding.etEmail.trimmedText.isValidEmail()) {
            binding.tilEmail.error = "Incorrect email"
            isValid = false
        }

        if (binding.etPassword.trimmedText.isEmpty()) {
            binding.tilPassword.error = getString(R.string.required)
            isValid = false
        } else if (binding.etPassword.trimmedText.length < 6) {
            binding.tilPassword.error = "At least 6 characters"
            isValid = false
        }

        if (binding.etFirstName.trimmedText.isEmpty()) {
            binding.tilFirstName.error = getString(R.string.required)
            isValid = false
        }

        if (binding.etLastName.trimmedText.isEmpty()) {
            binding.tilLastName.error = getString(R.string.required)
            isValid = false
        }

        return isValid
    }
}
