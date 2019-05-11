package com.marknkamau.justjava.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), SignUpView, View.OnClickListener {
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var address: String
    private lateinit var password: String

    private val presenter: SignUpPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignup.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun onClick(view: View) {
        when (view) {
            btnSignup -> createUser()
            tvLogin -> {
                finish()
            }
        }
    }

    override fun enableUserInteraction() {
        btnSignup.setBackgroundResource(R.drawable.large_button)
        btnSignup.isEnabled = true
        pbLoading.visibility = View.GONE
    }

    override fun disableUserInteraction() {
        pbLoading.visibility = View.VISIBLE
        btnSignup.setBackgroundResource(R.drawable.large_button_disabled)
        btnSignup.isEnabled = false
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() = finish()

    private fun createUser() {
        if (fieldsOk()) {
            presenter.createUser(email, password, name, phone, address)
        }
    }

    private fun fieldsOk(): Boolean {
        email = etEmailAddress.trimmedText
        password = etPassword.trimmedText

        name = etName.trimmedText
        phone = etPhone.trimmedText
        address = etDeliveryAddress.trimmedText

        val pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val matcher1 = pattern1.matcher(email)

        if (matcher1.matches()) {
            etEmailAddress.error = "Can not use @justjava.com"
            return false
        }
        if (email.isEmpty() || password.isEmpty()
                || name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            etEmailAddress.error = "Incorrect format"
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
            etPassword.error = "At least 6 characters"
            return false
        }
        return true
    }
}
