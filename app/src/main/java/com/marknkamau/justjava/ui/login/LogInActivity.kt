package com.marknkamau.justjava.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.completeSignUp.CompleteSignUpActivity
import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_log_in.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LogInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 99
    private val googleSignInClient: GoogleSignInClient by inject()
    private val signInViewModel by viewModel<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        initializeLoading()

        tilEmail.resetErrorOnChange(etEmail)
        tilPassword.resetErrorOnChange(etPassword)
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

    private fun signIn() {
        signInViewModel.signIn(etEmail.trimmedText, etPassword.trimmedText).observe(this, Observer { resource ->
            when(resource){
                is Resource.Success -> finish()
                is Resource.Failure -> toast(resource.message)
            }
        })
    }

    private fun signInWithGoogle(idToken: String) {
        signInViewModel.signInWithGoogle(idToken).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data.mobileNumber == null) { // Account has just been created
                        CompleteSignUpActivity.start(this, resource.data)
                    }
                    finish()
                }
                is Resource.Failure -> {
                    toast(resource.message)
                }
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
