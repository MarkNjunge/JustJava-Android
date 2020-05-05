package com.marknkamau.justjava.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.repository.UsersRepository
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun signInWithGoogle(idToken: String): LiveData<Resource<User>> {
        val liveData = MutableLiveData<Resource<User>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = authRepository.signInWithGoogle(idToken)

            if (liveData.value is Resource.Success) {
                usersRepository.updateFcmToken()
            }

            _loading.value = false
        }

        return liveData
    }

    fun signIn(email: String, password: String): LiveData<Resource<User>> {
        val liveData = MutableLiveData<Resource<User>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = authRepository.signIn(email, password)

            if (liveData.value is Resource.Success) {
                usersRepository.updateFcmToken()
            }

            _loading.value = false
        }

        return liveData
    }

    fun requestPasswordReset(email: String): LiveData<Resource<ApiResponse>> {
        val liveData = MutableLiveData<Resource<ApiResponse>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = authRepository.requestPasswordReset(email)
            _loading.value = false
        }

        return liveData
    }
}
