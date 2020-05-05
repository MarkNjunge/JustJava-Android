package com.marknkamau.justjava.ui.addressBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Address
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.model.User
import com.marknjunge.core.data.repository.UsersRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddressBookViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _user = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>> = _user

    fun getAddresses() {
        viewModelScope.launch {
            _loading.value = true
            usersRepository.getCurrentUser().collect { _user.value = it }
            _loading.value = false
        }
    }

    fun addAddress(address: Address): LiveData<Resource<Address>> {
        val liveData = MutableLiveData<Resource<Address>>()

        viewModelScope.launch {
            _loading.value = true
            liveData.value = usersRepository.addAddress(address)
            _loading.value = false
        }

        return liveData
    }

    fun deleteAddress(address: Address): LiveData<Resource<Unit>> {
        val livedata = MutableLiveData<Resource<Unit>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = usersRepository.deleteAddress(address)
            _loading.value = false
        }

        return livedata
    }
}
