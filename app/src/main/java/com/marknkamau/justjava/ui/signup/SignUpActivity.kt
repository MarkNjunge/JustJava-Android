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

class SignUpActivity : AppCompatActivity(), SignUpView, View.OnClickListener {
    val etEmailAddress: EditText by bindView(R.id.et_email_address)
    val etPassword: EditText by bindView(R.id.et_password)
    val imgViewPass: ImageView by bindView(R.id.img_view_pass)
    val etPasswordRepeat: EditText by bindView(R.id.et_password_repeat)
    val imgViewPassRpt: ImageView by bindView(R.id.img_view_pass_rpt)
    val etName: EditText by bindView(R.id.et_name)
    val etPhoneNumber: EditText by bindView(R.id.et_phone_number)
    val etDeliveryAddress: MultiAutoCompleteTextView by bindView(R.id.et_delivery_address)
    val btnSignUp: Button by bindView(R.id.btn_sign_up)
    val tvLogIn: TextView by bindView(R.id.tv_log_in)

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
        btnSignUp.setOnClickListener(this)
        tvLogIn.setOnClickListener(this)
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
            btnSignUp -> createUser()
            tvLogIn -> {
                startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
                finish()
            }
        }
    }

    override fun enableUserInteraction() {
        btnSignUp.setBackgroundResource(R.drawable.large_button)
        btnSignUp.isEnabled = true
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
        btnSignUp.setBackgroundResource(R.drawable.large_button_disabled)
        btnSignUp.isEnabled = false
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
        phone = etPhoneNumber.trimmedText()
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
