package com.marknkamau.justjava.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {

    private var isInEditMode = false
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile"

        observeLoading()
        observeUser()

        tilFirstName.resetErrorOnChange(etFirstName)
        tilLastName.resetErrorOnChange(etLastName)
        tilEmail.resetErrorOnChange(etEmail)
        tilMobile.resetErrorOnChange(etMobile)

        btnEdit.setOnClickListener {
            if (isInEditMode) {
                exitEditMode()
            } else {
                enterEditMode()
            }
        }
        btnUpdate.setOnClickListener {
            if (isValid()) {
                hideKeyboard()
                updateUser()
            }
        }

        profileViewModel.getCurrentUser()
    }

    private fun observeLoading() {
        profileViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeUser() {
        profileViewModel.user.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    etFirstName.setText(resource.data.firstName)
                    etLastName.setText(resource.data.lastName)
                    etEmail.setText(resource.data.email)
                    etMobile.setText(resource.data.mobileNumber)
                }
                is Resource.Failure -> {
                    toast(resource.message)
                }
            }
        })
    }

    private fun enterEditMode() {
        TransitionManager.beginDelayedTransition(rootProfileActivity)
        etFirstName.isEnabled = true
        etLastName.isEnabled = true
        etEmail.isEnabled = true
        etMobile.isEnabled = true
        btnEdit.text = "Cancel"
        isInEditMode = true
        btnUpdate.visibility = View.VISIBLE
    }

    private fun exitEditMode() {
        TransitionManager.beginDelayedTransition(rootProfileActivity)
        etFirstName.isEnabled = false
        etLastName.isEnabled = false
        etEmail.isEnabled = false
        etMobile.isEnabled = false
        btnEdit.text = "Edit"
        isInEditMode = false
        btnUpdate.visibility = View.GONE
    }

    private fun updateUser() {
        profileViewModel.updateUser(
            etFirstName.trimmedText,
            etLastName.trimmedText,
            etMobile.trimmedText,
            etEmail.trimmedText
        ).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> toast("Profile updated")
                is Resource.Failure -> toast(resource.message)
            }
            exitEditMode()
        })
    }

    private fun isValid(): Boolean {
        var valid = true

        if (etFirstName.trimmedText.isEmpty()) {
            tilFirstName.error = "Required"
            valid = false
        }

        if (etLastName.trimmedText.isEmpty()) {
            tilLastName.error = "Required"
            valid = false
        }

        if (etMobile.trimmedText.isEmpty()) {
            tilMobile.error = "Required"
            valid = false
        } else if (etMobile.trimmedText.length != 12) {
            tilMobile.error = "A valid mobile number is required"
            valid = false
        }

        if (etEmail.trimmedText.isEmpty()) {
            tilEmail.error = "Required"
            valid = false
        } else if (!etEmail.trimmedText.isValidEmail()) {
            tilEmail.error = "A valid email is required"
            valid = false
        }

        return valid
    }
}