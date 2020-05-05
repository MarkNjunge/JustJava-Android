package com.marknkamau.justjava.ui.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.models.AppProduct
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val dbRepository: DbRepository) : ViewModel() {
    fun addItemToCart(item: AppProduct, quantity: Int): LiveData<Unit> {
        val livedata = MutableLiveData<Unit>()

        viewModelScope.launch {
            livedata.value = dbRepository.saveItemToCart(item, quantity)
        }

        return livedata
    }
}
