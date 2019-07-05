package com.marknkamau.justjava.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.utils.onTextChanged
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), SignUpView {
    private val presenter: SignUpPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignupSignUp.setOnClickListener { createUser() }
        btnLoginSignUp.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
            finish()
        }

        etEmailAddressSignUp.onTextChanged { tilEmailAddressSignUp.error = null }
        etPasswordSignUp.onTextChanged { tilPasswordSignUp.error = null }
        etNameSignUp.onTextChanged { tilNameSignUp.error = null }
        etPhoneSignUp.onTextChanged { tilPhoneSignUp.error = null }
        etDeliveryAddressSignUp.onTextChanged { tilDeliveryAddressSignUp.error = null }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun enableUserInteraction() {
        btnSignupSignUp.isEnabled = true
        btnLoginSignUp.isEnabled = true
        pbLoadingSignUp.visibility = View.GONE
    }

    override fun disableUserInteraction() {
        btnSignupSignUp.isEnabled = false
        btnLoginSignUp.isEnabled = false
        pbLoadingSignUp.visibility = View.VISIBLE
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishActivity() = finish()

    private fun createUser() {
        if (isValid()) {
            presenter.createUser(etEmailAddressSignUp.trimmedText, etPasswordSignUp.trimmedText, etNameSignUp.trimmedText, etPhoneSignUp.trimmedText, etDeliveryAddressSignUp.trimmedText)
        }
    }

    private fun isValid(): Boolean {
        var isValid = true

        val justJavaEmailPattern = Pattern.compile("^([a-zA-Z0-9_.-])+@justjava.com+")
        val justJavaEmailMatcher = justJavaEmailPattern.matcher(etEmailAddressSignUp.trimmedText)

        if (etEmailAddressSignUp.trimmedText.isEmpty()) {
            tilEmailAddressSignUp.error = getString(R.string.required)
            isValid = false
        } else if (justJavaEmailMatcher.matches()) {
            tilEmailAddressSignUp.error = "Can not use @justjava.com"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmailAddressSignUp.trimmedText).matches()) {
            tilEmailAddressSignUp.error = "Incorrect email"
            isValid = false
        }

        if (etPasswordSignUp.trimmedText.isEmpty()) {
            tilPasswordSignUp.error = getString(R.string.required)
            isValid = false
        } else if (etPasswordSignUp.trimmedText.length < 6) {
            tilPasswordSignUp.error = "At least 6 characters"
            isValid = false
        }

        if (etNameSignUp.trimmedText.isEmpty()) {
            tilNameSignUp.error = getString(R.string.required)
            isValid = false
        }

        if (etPhoneSignUp.trimmedText.isEmpty()) {
            tilPhoneSignUp.error = getString(R.string.required)
            isValid = false
        }

        if (etDeliveryAddressSignUp.trimmedText.isEmpty()) {
            tilDeliveryAddressSignUp.error = getString(R.string.required)
            isValid = false
        }

        return isValid
    }
}
