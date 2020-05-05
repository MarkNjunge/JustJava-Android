package com.marknkamau.justjava.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.repository.OrdersRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.CartItem
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val dbRepository: DbRepository,
    private val ordersRepository: OrdersRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _items = MutableLiveData<List<CartItem>>()
    val items: LiveData<List<CartItem>> = _items

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun isSignedIn() = preferencesRepository.isSignedIn

    fun getUser() = preferencesRepository.user!!

    fun getCartItems() {
        viewModelScope.launch {
            _items.value = dbRepository.getCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            dbRepository.clearCart()
        }
    }

    fun placeOrder(
        paymentMethod: PaymentMethod,
        address: Address,
        additionalComments: String? = null
    ): LiveData<Resource<Order>> {
        val liveData = MutableLiveData<Resource<Order>>()

        viewModelScope.launch {
            _loading.value = true
            val items = dbRepository.getCartItems().map { item ->
                val options = item.options.map { PlaceOrderItemOptionsDto(it.choiceId, it.optionId) }
                PlaceOrderItemDto(item.cartItem.quantity, item.cartItem.productId, options)
            }

            liveData.value =
                ordersRepository.placeOrder(PlaceOrderDto(paymentMethod.s, items, address.id, additionalComments))

            _loading.value = false
        }

        return liveData
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
}
