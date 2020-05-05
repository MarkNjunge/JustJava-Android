package com.marknkamau.justjava.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.repository.CartRepository
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.CartItem
import kotlinx.coroutines.launch

class CartViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val dbRepository: DbRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {

    private val _items = MutableLiveData<List<CartItem>>()
    val items: LiveData<List<CartItem>> = _items

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun isSignedIn() = preferencesRepository.isSignedIn

    fun getCartItems() {
        viewModelScope.launch {
            _items.value = dbRepository.getCartItems()
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            dbRepository.deleteItemFromCart(item)
            _items.value = dbRepository.getCartItems()
        }
    }

    fun verifyOrder(items: List<CartItem>): LiveData<Resource<List<VerifyOrderResponse>>> {
        val livedata = MutableLiveData<Resource<List<VerifyOrderResponse>>>()

        viewModelScope.launch {
            _loading.value = true
            val verificationDto = items.mapIndexed { index, item ->
                val options =
                    item.options.map { VerifyOrderItemOptionDto(it.choiceId, it.optionId, it.optionPrice) }
                VerifyOrderItemDto(index, item.cartItem.productId, item.cartItem.productBasePrice, options)
            }

            livedata.value = cartRepository.verifyOrder(VerifyOrderDto(verificationDto))
            _loading.value = false
        }

        return livedata
    }
}
