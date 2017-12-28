package com.marknkamau.justjavastaff.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast

import com.marknkamau.justjavastaff.JustJavaStaffApp
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.ui.main.MainActivity

import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity(), LoginView, View.OnClickListener {

    private var passVisible = false
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        ButterKnife.bind(this)

        val auth = (application as JustJavaStaffApp).auth
        presenter = LoginPresenter(auth, this)

        if (auth.currentEmployee() != null) {
            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            finish()
        }

        btnLogIn.setOnClickListener(this)
        imgVisibility.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgVisibility -> if (passVisible) {
                etPassword.transformationMethod = PasswordTransformationMethod()
                imgVisibility.setImageResource(R.drawable.ic_visibility_off)
                passVisible = false
            } else {
                imgVisibility.setImageResource(R.drawable.ic_visibility)
                etPassword.transformationMethod = null
                passVisible = true
            }
            R.id.btnLogIn -> if (validateFields())
                signUserIn()
        }
    }

    private fun signUserIn() {
        disableButtons()
        pbLoading.visibility = View.VISIBLE

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        presenter.signIn(email, password)
    }

    private fun disableButtons() {
        btnLogIn.isEnabled = false
    }

    private fun enableButtons() {
        btnLogIn.isEnabled = true
    }

    private fun validateFields(): Boolean {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

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

    override fun displayMessage(message: String) {
        enableButtons()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSignedIn() {
        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
        finish()
    }
}
