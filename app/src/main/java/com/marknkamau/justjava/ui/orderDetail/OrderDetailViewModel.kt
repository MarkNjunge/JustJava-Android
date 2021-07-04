package com.marknkamau.justjava.ui.orderDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.PaymentMethod
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _order = MutableLiveData<Resource<Order>>()
    val order: LiveData<Resource<Order>> = _order

    fun getOrder(id: String) {
        viewModelScope.launch {
            _loading.value = true
            _order.value = ordersRepository.getOrderById(id)
            _loading.value = false
        }
    }

    fun changePaymentMethod(id: String, method: PaymentMethod): LiveData<Resource<ApiResponse>> {
        val livedata = MutableLiveData<Resource<ApiResponse>>()
        viewModelScope.launch {
            _loading.value = true
            livedata.value = ordersRepository.changePaymentMethod(id, method)
            _loading.value = false
        }

        return livedata
    }

    fun getUser() = preferencesRepository.user!!
}
