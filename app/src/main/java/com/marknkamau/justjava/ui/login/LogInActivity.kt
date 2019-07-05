package com.marknkamau.justjava.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.signup.SignUpActivity
import com.marknkamau.justjava.utils.hideKeyboard
import com.marknkamau.justjava.utils.onTextChanged
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_log_in.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LogInActivity : AppCompatActivity(), LogInView {
    private lateinit var email: String
    private val presenter: LogInPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        presenter.checkSignInStatus()

        btnLoginLogin.setOnClickListener { signIn() }
        tvForgotPassLogin.setOnClickListener { resetUserPassword() }
        btnSignUpLogin.setOnClickListener {
            startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            finish()
        }

        etEmailLogin.onTextChanged {
            tilEmailLogin.error = null
        }
        etPasswordLogin.onTextChanged {
            tilPasswordLogin.error = null
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.checkSignInStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun closeActivity() {
        finish()
    }

    override fun signIn() {
        if (validateFields()) {
            hideKeyboard()
            disableButtons()
            presenter.signIn(etEmailLogin.trimmedText, etPasswordLogin.trimmedText)
        }
    }

    override fun resetUserPassword() {
        email = etEmailLogin.trimmedText

        if (TextUtils.isEmpty(email)) {
            tilEmailLogin.error = "Enter your email address"
        } else {
            disableButtons()
            presenter.resetUserPassword(email)
        }
    }

    override fun displayMessage(message: String?) {
        enableButtons()
        dismissLoading()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun finishSignIn() {
        finish()
    }

    private fun disableButtons() {
        btnLoginLogin.isEnabled = false
        btnSignUpLogin.isEnabled = false
        tvForgotPassLogin.isEnabled = false
    }

    private fun enableButtons() {
        btnLoginLogin.isEnabled = true
        btnSignUpLogin.isEnabled = true
        tvForgotPassLogin.isEnabled = true
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (etEmailLogin.trimmedText.isEmpty()) {
            tilEmailLogin.error = "Required"
            isValid = false
        }
        if (etPasswordLogin.trimmedText.isEmpty()) {
            tilPasswordLogin.error = "Required"
            isValid = false
        }

        return isValid
    }

}
