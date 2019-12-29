package com.marknkamau.justjava.ui.orderDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val ordersRepository: OrdersRepository, private val preferencesRepository: PreferencesRepository) : ViewModel() {
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

    fun getUser() = preferencesRepository.user!!
}