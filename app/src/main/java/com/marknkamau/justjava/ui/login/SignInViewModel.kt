package com.marknkamau.justjava.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.UsersRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel(private val authRepository: AuthRepository, private val usersRepository: UsersRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun signInWithGoogle(idToken: String): LiveData<Resource<User>> {
        val liveData = MutableLiveData<Resource<User>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = authRepository.signInWithGoogle(idToken)

            // Update FCM token
            val idResult = FirebaseInstanceId.getInstance().instanceId.await()
            usersRepository.updateFcmToken(idResult.token)

            _loading.value = false
        }

        return liveData
    }

    fun signIn(email: String, password: String): LiveData<Resource<User>> {
        val liveData = MutableLiveData<Resource<User>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = authRepository.signIn(email, password)

            // Update FCM token
            val idResult = FirebaseInstanceId.getInstance().instanceId.await()
            usersRepository.updateFcmToken(idResult.token)

            _loading.value = false
        }

        return liveData
    }
}