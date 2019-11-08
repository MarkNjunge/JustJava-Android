package com.marknkamau.justjava.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.repository.ProductsRepository
import com.marknjunge.core.model.Product
import kotlinx.coroutines.launch

class MainViewModel(private val productsRepository: ProductsRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun getProducts() {
        viewModelScope.launch {
            _loading.value = true
            _products.value = productsRepository.getProducts()
            _loading.value = false
        }
    }
}