package com.marknkamau.justjava.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.AuthRepository
import com.marknjunge.core.data.repository.UsersRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    fun getCurrentUser() {
        viewModelScope.launch {
            _loading.value = true
            usersRepository.getCurrentUser().collect { _user.value = it }
            _loading.value = false
        }
    }

    fun updateUser(firstName: String, lastName: String, mobile: String, email: String): LiveData<Resource<Unit>> {
        val livedata = MutableLiveData<Resource<Unit>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = usersRepository.updateUser(firstName, lastName, mobile, email)
            _loading.value = false
        }

        return livedata
    }

    fun signOut(): LiveData<Resource<Unit>> {
        val livedata = MutableLiveData<Resource<Unit>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = authRepository.signOut()
            _loading.value = false
        }

        return livedata
    }

    fun deleteAccount(): LiveData<Resource<Unit>> {
        val livedata = MutableLiveData<Resource<Unit>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = usersRepository.deleteUser()
            _loading.value = false
        }

        return livedata
    }
}
