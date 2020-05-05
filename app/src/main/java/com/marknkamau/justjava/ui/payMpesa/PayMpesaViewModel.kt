package com.marknkamau.justjava.ui.payMpesa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.PaymentsRepository
import com.marknjunge.core.data.model.ApiResponse
import kotlinx.coroutines.launch

class PayMpesaViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val paymentsRepository: PaymentsRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getUser() = preferencesRepository.user!!

    fun payMpesa(mobileNumber: String, orderId: String): LiveData<Resource<ApiResponse>> {
        val livedata = MutableLiveData<Resource<ApiResponse>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = paymentsRepository.requestMpesa(mobileNumber, orderId)
            _loading.value = false
        }

        return livedata
    }
}
