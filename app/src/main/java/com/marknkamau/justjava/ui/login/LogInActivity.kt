package com.marknkamau.justjava.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.signup.SignUpActivity

import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity(), LogInView, View.OnClickListener {
    private lateinit var email: String
    private var passVisible = false
    private lateinit var presenter: LogInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val authService = (application as JustJavaApp).authService
        val database = (application as JustJavaApp).databaseService

        presenter = LogInPresenter(this, preferencesRepository, authService, database)
        presenter.checkSignInStatus()

        imgVisibility.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        tvForgotPass.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.checkSignInStatus()
    }

    override fun onClick(view: View) {
        when (view) {
            imgVisibility ->
                if (passVisible) {
                    etPassword.transformationMethod = PasswordTransformationMethod()
                    imgVisibility.setImageResource(R.drawable.ic_visibility_off)
                    passVisible = false
                } else {
                    imgVisibility.setImageResource(R.drawable.ic_visibility)
                    etPassword.transformationMethod = null
                    passVisible = true
                }
            btnLogin -> signIn()
            tvForgotPass -> resetUserPassword()
            tvSignup -> {
                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            }
        }
    }

    override fun closeActivity() {
        finish()
    }

    override fun signIn() {
        if (validateFields()) {
            disableButtons()
            presenter.signIn(etEmail.text.toString().trim { it <= ' ' }, etPassword.text.toString().trim { it <= ' ' })
        }
    }

    override fun resetUserPassword() {
        disableButtons()
        email = etEmail.trimmedText

        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Enter your email address"
            enableButtons()
        } else {
            presenter.resetUserPassword(email)
        }
    }

    override fun displayMessage(message: String?) {
        enableButtons()
        dismissDialog()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showDialog() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun dismissDialog() {
        pbLoading.visibility = View.GONE
    }

    override fun finishSignIn() {
        finish()
    }

    private fun disableButtons() {
        btnLogin.isEnabled = false
        tvSignup.isEnabled = false
        tvForgotPass.isEnabled = false
    }

    private fun enableButtons() {
        btnLogin.isEnabled = true
        tvSignup.isEnabled = true
        tvForgotPass.isEnabled = true
    }

    private fun validateFields(): Boolean {
        email = etEmail.trimmedText
        val password = etPassword.trimmedText

        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Required"
            return false
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Required"
            return false
        }

        return true
    }

}
