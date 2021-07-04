package com.marknkamau.justjava.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.transition.TransitionManager
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.databinding.ActivityProfileBinding
import com.marknkamau.justjava.ui.ToolbarActivity
import com.marknkamau.justjava.ui.addressBook.AddressBookActivity
import com.marknkamau.justjava.ui.orders.OrdersActivity
import com.marknkamau.justjava.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : ToolbarActivity() {

    private var isInEditMode = false
    private val profileViewModel: ProfileViewModel by viewModels()
    override var requiresSignedIn = true
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"

        observeLoading()
        observeUser()

        binding.tilFirstName.resetErrorOnChange(binding.etFirstName)
        binding.tilLastName.resetErrorOnChange(binding.etLastName)
        binding.tilEmail.resetErrorOnChange(binding.etEmail)
        binding.tilMobile.resetErrorOnChange(binding.etMobile)

        binding.btnEdit.setOnClickListener {
            if (isInEditMode) {
                exitEditMode()
            } else {
                enterEditMode()
            }
        }
        binding.btnUpdate.setOnClickListener {
            if (isValid()) {
                hideKeyboard()
                updateUser()
            }
        }
        binding.llAddressBook.setOnClickListener {
            startActivity(Intent(this, AddressBookActivity::class.java))
        }
        binding.llOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            signOut()
        }
        binding.btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Delete") { _, _ -> deleteAccount() }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        profileViewModel.getCurrentUser()
    }

    private fun observeLoading() {
        profileViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeUser() {
        profileViewModel.user.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.etFirstName.setText(resource.data.firstName)
                    binding.etLastName.setText(resource.data.lastName)
                    binding.etEmail.setText(resource.data.email)
                    binding.etMobile.setText(resource.data.mobileNumber)
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun enterEditMode() {
        TransitionManager.beginDelayedTransition(binding.root)
        binding.etFirstName.isEnabled = true
        binding.etLastName.isEnabled = true
        binding.etEmail.isEnabled = true
        binding.etMobile.isEnabled = true
        binding.btnEdit.text = "Cancel"
        isInEditMode = true
        binding.btnUpdate.visibility = View.VISIBLE
    }

    private fun exitEditMode() {
        TransitionManager.beginDelayedTransition(binding.root)
        binding.etFirstName.isEnabled = false
        binding.etLastName.isEnabled = false
        binding.etEmail.isEnabled = false
        binding.etMobile.isEnabled = false
        binding.btnEdit.text = "Edit"
        isInEditMode = false
        binding.btnUpdate.visibility = View.GONE
    }

    private fun updateUser() {
        profileViewModel.updateUser(
            binding.etFirstName.trimmedText,
            binding.etLastName.trimmedText,
            binding.etMobile.trimmedText,
            binding.etEmail.trimmedText
        ).observe(this, { resource ->
            when (resource) {
                is Resource.Success -> toast("Profile updated")
                is Resource.Failure -> handleApiError(resource)
            }
            exitEditMode()
        })
    }

    private fun signOut() {
        profileViewModel.signOut().observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    invalidateOptionsMenu()
                    finish()
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun deleteAccount() {
        profileViewModel.deleteAccount().observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    finish()
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun isValid(): Boolean {
        var valid = true

        if (binding.etFirstName.trimmedText.isEmpty()) {
            binding.tilFirstName.error = "Required"
            valid = false
        }

        if (binding.etLastName.trimmedText.isEmpty()) {
            binding.tilLastName.error = "Required"
            valid = false
        }

        if (binding.etEmail.trimmedText.isEmpty()) {
            binding.tilEmail.error = "Required"
            valid = false
        } else if (!binding.etEmail.trimmedText.isValidEmail()) {
            binding.tilEmail.error = "A valid email is required"
            valid = false
        }

        return valid
    }
}
