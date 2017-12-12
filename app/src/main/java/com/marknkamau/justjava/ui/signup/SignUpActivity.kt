package com.marknkamau.justjava.ui.signup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.login.LogInActivity

import java.util.regex.Pattern
import com.marknkamau.justjava.utils.bindView
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), SignUpView, View.OnClickListener {
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var address: String
    private lateinit var password: String
    private var passVisible = false
    private var passRptVisible = false
    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val auth = (application as JustJavaApp).authService
        val database = (application as JustJavaApp).databaseService

        presenter = SignUpPresenter(this, preferencesRepository, auth, database)

        imgViewPass.setOnClickListener(this)
        imgViewPassRpt.setOnClickListener(this)
        btnSignup.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            imgViewPass ->
                if (passVisible) {
                    etPassword.transformationMethod = PasswordTransformationMethod()
                    imgViewPass.setImageResource(R.drawable.ic_visibility_off)
                    passVisible = false
                } else {
                    etPassword.transformationMethod = null
                    imgViewPass.setImageResource(R.drawable.ic_visibility)
                    passVisible = true
                }
            imgViewPassRpt ->
                if (passRptVisible) {
                    etPasswordRepeat.transformationMethod = PasswordTransformationMethod()
                    imgViewPassRpt.setImageResource(R.drawable.ic_visibility_off)
                    passRptVisible = false
                } else {
                    etPasswordRepeat.transformationMethod = null
                    imgViewPassRpt.setImageResource(R.drawable.ic_visibility)
                    passRptVisible = true
                }
            btnSignup -> createUser()
            tvLogin -> {
                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
                finish()
            }
        }
    }

    override fun enableUserInteraction() {
        btnSignup.setBackgroundResource(R.drawable.large_button)
        btnSignup.isEnabled = true
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun disableUserInteraction() {
        progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setTitle(null)
        progressDialog.setMessage("Creating user")
        progressDialog.show()
        btnSignup.setBackgroundResource(R.drawable.large_button_disabled)
        btnSignup.isEnabled = false
    }

    override fun displayMessage(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun finishActivity() = finish()

    private fun createUser() {
        if (fieldsOk()) {
            presenter.createUser(email, password, name, phone, address)
        }
    }

    private fun fieldsOk(): Boolean {
        email = etEmailAddress.trimmedText()
        password = etPassword.trimmedText()
        val passwordRpt = etPasswordRepeat.trimmedText()

        name = etName.trimmedText()
        phone = etPhone.trimmedText()
        address = etDeliveryAddress.trimmedText()

        val pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val matcher1 = pattern1.matcher(email)

        if (matcher1.matches()) {
            etEmailAddress.error = "Can not use @justjava.com"
            return false
        }
        if (email.isEmpty() || password.isEmpty() || passwordRpt.isEmpty()
                || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.error = "Incorrect format"
            return false
        }
        if (password.length < 6) {
            etPassword.error = "At least 6 characters"
            return false
        }
        if (password != passwordRpt) {
            etPasswordRepeat.error = "Passwords do no match"
            return false
        }
        return true
    }
}
