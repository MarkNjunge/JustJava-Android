package com.marknkamau.justjava.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity(), LogInView, View.OnClickListener {
    private lateinit var email: String
    private lateinit var presenter: LogInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val authService = (application as JustJavaApp).authService
        val database = (application as JustJavaApp).databaseService

        presenter = LogInPresenter(this, preferencesRepository, authService, database)
        presenter.checkSignInStatus()

        btnLogin.setOnClickListener(this)
        tvForgotPass.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.checkSignInStatus()
    }

    override fun onClick(view: View) {
        when (view) {
            btnLogin -> signIn()
            tvForgotPass -> resetUserPassword()
            btnSignUp -> {
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
        btnSignUp.isEnabled = false
        tvForgotPass.isEnabled = false
    }

    private fun enableButtons() {
        btnLogin.isEnabled = true
        btnSignUp.isEnabled = true
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
