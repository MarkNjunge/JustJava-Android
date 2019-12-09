package com.marknkamau.justjava.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun signUp(firstName: String, lastName: String, mobile: String, email: String, password: String): LiveData<Resource<User>> {
        val livedata = MutableLiveData<Resource<User>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = authRepository.signUp(firstName, lastName, mobile, email, password)
            _loading.value = false
        }

        return livedata
    }
}