package com.marknkamau.justjava.ui.completeSignUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.data.network.FirebaseService
import kotlinx.coroutines.launch

class CompleteSignUpViewModel(private val usersRepository: UsersRepository, private val firebaseService: FirebaseService) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun completeSignUp(firstName: String, lastName: String, mobile: String, email: String): LiveData<Resource<Unit>> {
        val liveData = MutableLiveData<Resource<Unit>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = usersRepository.updateUser(firstName, lastName, mobile, email)
            _loading.value = false
        }

        return liveData
    }
}