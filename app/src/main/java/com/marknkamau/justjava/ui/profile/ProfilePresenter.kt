package com.marknkamau.justjava.ui.profile

import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.data.firebase.UserService
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

internal class ProfilePresenter(private val view: ProfileView,
                                private val preferencesRepository: PreferencesRepository,
                                private val orderService: OrderService,
                                private val userService: UserService,
                                mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    private lateinit var userDetails: UserDetails

    fun getUserDetails() {
//        userDetails = preferencesRepository.getUserDetails()
        view.displayUserDetails(userDetails)
    }

    fun getPreviousOrders() {
        uiScope.launch {
            try {
//                val previousOrders = orderService.getPreviousOrders(authenticationService.getCurrentUser().userId)
//                if (previousOrders.isEmpty()) {
//                    view.displayNoPreviousOrders()
//                } else {
//                    val sorted = previousOrders.sortedBy { it.date }.reversed().take(3).toMutableList()
//                    view.displayPreviousOrders(sorted)
//                }
            } catch (e: Exception) {
                view.displayMessage(e.message ?: "Error getting previous orders")
            }
        }
    }

    fun updateUserDetails(name: String, phone: String, address: String) {
        view.showProfileProgressBar()
        uiScope.launch {
            try {
//                authenticationService.setUserDisplayName(name)
                userService.updateUserDetails(userDetails.id, name, phone, address)
                val newUserDetails = UserDetails(userDetails.id, userDetails.email, name, phone, address)

//                preferencesRepository.saveUserDetails(newUserDetails)
                view.hideProfileProgressBar()
                view.displayMessage("Profile updated")
            } catch (e: Exception) {
                Timber.e(e)
                view.hideProfileProgressBar()
                view.displayMessage(e.message ?: "Error upating profile")
            }
        }
    }

}
