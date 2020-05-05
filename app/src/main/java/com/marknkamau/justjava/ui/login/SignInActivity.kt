package com.marknkamau.justjava.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignInActivity : BaseActivity() {

    companion object {
        private const val RC_SIGN_IN = 99
    }

    private val googleSignInClient: GoogleSignInClient by inject()
    private val signInViewModel by viewModel<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initializeLoading()

        tilEmail.resetErrorOnChange(etEmail)
        tilPassword.resetErrorOnChange(etPassword)
        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
        btnSignInGoogle.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
            hideKeyboard()
        }
        llSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        btnSignIn.setOnClickListener {
            if (isValid()) {
                hideKeyboard()
                signIn()
            }
        }
    }

    private fun initializeLoading() {
        signInViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.idToken?.let { token ->
                    signInWithGoogle(token)
                }
            } catch (e: ApiException) {
                Timber.e("signInResult:failed code=${e.statusCode}")
                toast("Sign in with Google failed. Use password instead.")
            }
        }
    }

    private fun showForgotPasswordDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Reset password")

        val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_reset_password, findViewById(android.R.id.content), false)
        val input: EditText = view.findViewById(R.id.etEmailAddress)
        dialog.setView(view)

        dialog.setPositiveButton("Reset") { _, _ ->
            Timber.d(input.text.toString())
            val email = input.text.toString().trim()
            if (email.isNotEmpty()) {
                requestPasswordReset(email)
            }
        }
        dialog.setNegativeButton("Cancel") { d, _ ->
            d.cancel()
        }

        dialog.show()
    }

    private fun requestPasswordReset(email: String) {
        signInViewModel.requestPasswordReset(email).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> toast("A password reset email has been sent")
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun signIn() {
        signInViewModel.signIn(etEmail.trimmedText, etPassword.trimmedText).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> finish()
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun signInWithGoogle(idToken: String) {
        signInViewModel.signInWithGoogle(idToken).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> finish()
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun isValid(): Boolean {
        var valid = true

        if (etEmail.trimmedText.isEmpty()) {
            tilEmail.error = "Required"
            valid = false
        } else if (!etEmail.trimmedText.isValidEmail()) {
            tilEmail.error = "Incorrect email"
            valid = false
        }

        if (etPassword.trimmedText.isEmpty()) {
            tilPassword.error = "Required"
            valid = false
        }

        return valid
    }
}
