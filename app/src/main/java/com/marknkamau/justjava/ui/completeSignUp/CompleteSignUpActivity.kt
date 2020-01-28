package com.marknkamau.justjava.ui.completeSignUp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_complete_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompleteSignUpActivity : AppCompatActivity() {

    companion object {
        private const val USER_KEY = "user"

        fun start(context: Context, user: User) {
            val intent = Intent(context, CompleteSignUpActivity::class.java).apply {
                putExtra(USER_KEY, user)
            }
            context.startActivity(intent)
        }
    }

    private val completeViewModel: CompleteSignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_sign_up)

        val user = intent.extras!![USER_KEY] as User
        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        etEmail.setText(user.email)

        observeLoading()

        tilFirstName.resetErrorOnChange(etFirstName)
        tilLastName.resetErrorOnChange(etLastName)
        tilMobileNumber.resetErrorOnChange(etMobileNumber)
        tilEmail.resetErrorOnChange(etEmail)
        btnComplete.setOnClickListener {
            if (isValid()) {
                completeSignUp()
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed() // Do nothing
    }

    private fun observeLoading() {
        completeViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun completeSignUp() {
        completeViewModel.completeSignUp(
            etFirstName.trimmedText,
            etLastName.trimmedText,
            etMobileNumber.trimmedText,
            etEmail.trimmedText
        ).observe(this, Observer { resource ->
            when(resource){
                is Resource.Success -> finish()
                is Resource.Failure -> toast(resource.message)
            }
        })
    }

    private fun isValid(): Boolean {
        var valid = true

        etMobileNumber.setText(PhoneNumberUtils.sanitize(etMobileNumber.text.toString()))

        if (etFirstName.trimmedText.isEmpty()) {
            valid = false
            tilFirstName.error = "Required"
        }

        if (etLastName.trimmedText.isEmpty()) {
            valid = false
            tilLastName.error = "Required"
        }

        if (etMobileNumber.trimmedText.isEmpty()) {
            valid = false
            tilMobileNumber.error = "Required"
        }

        if (etEmail.trimmedText.isEmpty()) {
            valid = false
            tilEmail.error = "Required"
        } else if (!etEmail.trimmedText.isValidEmail()) {
            valid = false
            tilEmail.error = "Please enter a valid email address"
        }

        return valid
    }
}
