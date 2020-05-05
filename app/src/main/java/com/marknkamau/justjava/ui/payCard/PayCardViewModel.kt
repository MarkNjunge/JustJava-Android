package com.marknkamau.justjava.ui.payCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.PaymentsRepository
import com.marknjunge.core.data.model.ApiResponse
import com.marknjunge.core.data.model.CardDetails
import kotlinx.coroutines.launch

class PayCardViewModel(
    private val paymentsRepository: PaymentsRepository
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun initiateCardPayment(
        orderId: String,
        cardNo: String,
        expiryMonth: String,
        expiryYear: String,
        cvv: String
    ): LiveData<Resource<ApiResponse>> {
        val livedata = MutableLiveData<Resource<ApiResponse>>()

        viewModelScope.launch {
            _loading.value = true
            livedata.value = paymentsRepository.initiateCardPayment(
                orderId,
                CardDetails(
                    cardNo,
                    cvv,
                    expiryMonth,
                    expiryYear,
                    "07205",
                    "Hillside",
                    "470 Mundet PI",
                    "NJ",
                    "US"
                )
            )
            _loading.value = false
        }

        return livedata
    }
}
