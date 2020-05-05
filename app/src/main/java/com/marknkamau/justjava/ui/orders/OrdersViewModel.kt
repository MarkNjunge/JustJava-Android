package com.marknkamau.justjava.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.OrdersRepository
import kotlinx.coroutines.launch

class OrdersViewModel(private val ordersRepository: OrdersRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _orders = MutableLiveData<Resource<List<Order>>>()
    val orders: LiveData<Resource<List<Order>>> = _orders

    fun getOrders() {
        viewModelScope.launch {
            _loading.value = true
            _orders.value = ordersRepository.getOrders()
            _loading.value = false
        }
    }
}
