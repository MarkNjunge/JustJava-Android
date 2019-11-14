package com.marknkamau.justjava.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.CartItem
import kotlinx.coroutines.launch

class CartViewModel(private val dbRepository: DbRepository) : ViewModel() {

    private val _products = MutableLiveData<List<CartItem>>()
    val products: LiveData<List<CartItem>> = _products

    fun getCartItems() {
        viewModelScope.launch {
            _products.value = dbRepository.getCartItems()
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            dbRepository.deleteItemFromCart(item)
            _products.value = dbRepository.getCartItems()
        }
    }
}