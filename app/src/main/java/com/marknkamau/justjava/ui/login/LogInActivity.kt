package com.marknkamau.justjava.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.signup.SignUpActivity

import com.marknkamau.justjava.utils.bindView
import com.marknkamau.justjava.utils.trimmedText

class LogInActivity : AppCompatActivity(), LogInView, View.OnClickListener {
    val etEmail: EditText by bindView(R.id.et_email)
    val etPassword: EditText by bindView(R.id.et_password)
    val btnLogIn: Button by bindView(R.id.btn_log_in)
    val imgVisibility: ImageView by bindView(R.id.img_visibility)
    val tvForgotPass: TextView by bindView(R.id.tv_forgot_pass)
    val tvSignUp: TextView by bindView(R.id.tv_sign_up)

    private lateinit var email: String
    private var passVisible = false
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: LogInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val authService = (application as JustJavaApp).authService
        presenter = LogInPresenter(this, preferencesRepository, authService)
        presenter.checkSignInStatus()

        imgVisibility.setOnClickListener(this)
        btnLogIn.setOnClickListener(this)
        tvForgotPass.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
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
            btnLogIn -> signIn()
            tvForgotPass -> resetUserPassword()
            tvSignUp -> {
                startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
                finish()
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
        email = etEmail.trimmedText()

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
        // TODO remove progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setTitle(null)
        progressDialog.setMessage("Authenticating")
        progressDialog.show()
    }

    override fun dismissDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }

    override fun finishSignUp() {
        finish()
    }

    private fun disableButtons() {
        btnLogIn.isEnabled = false
        tvSignUp.isEnabled = false
        tvForgotPass.isEnabled = false
    }

    private fun enableButtons() {
        btnLogIn.isEnabled = true
        tvSignUp.isEnabled = true
        tvForgotPass.isEnabled = true
    }

    private fun validateFields(): Boolean {
        email = etEmail.trimmedText()
        val password = etPassword.trimmedText()

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
