package com.marknkamau.justjava.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.login.SignInActivity
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class SignUpActivity : BaseActivity() {

    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        observeLoading()

        tilEmail.resetErrorOnChange(etEmail)
        tilPassword.resetErrorOnChange(etPassword)
        tilFirstName.resetErrorOnChange(etFirstName)
        tilLastName.resetErrorOnChange(etLastName)

        btnSignup.setOnClickListener {
            if (isValid()) {
                hideKeyboard()
                signUp()
            }
        }
        llSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun observeLoading() {
        signUpViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun signUp() {
        signUpViewModel.signUp(
            etFirstName.trimmedText,
            etLastName.trimmedText,
            etEmail.trimmedText,
            etPassword.trimmedText
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
        val justJavaEmailMatcher = justJavaEmailPattern.matcher(etEmail.trimmedText)

        if (etEmail.trimmedText.isEmpty()) {
            tilEmail.error = getString(R.string.required)
            isValid = false
        } else if (justJavaEmailMatcher.matches()) {
            tilEmail.error = "Can not use @justjava.com"
            isValid = false
        } else if (!etEmail.trimmedText.isValidEmail()) {
            tilEmail.error = "Incorrect email"
            isValid = false
        }

        if (etPassword.trimmedText.isEmpty()) {
            tilPassword.error = getString(R.string.required)
            isValid = false
        } else if (etPassword.trimmedText.length < 6) {
            tilPassword.error = "At least 6 characters"
            isValid = false
        }

        if (etFirstName.trimmedText.isEmpty()) {
            tilFirstName.error = getString(R.string.required)
            isValid = false
        }

        if (etLastName.trimmedText.isEmpty()) {
            tilLastName.error = getString(R.string.required)
            isValid = false
        }

        return isValid
    }
}
