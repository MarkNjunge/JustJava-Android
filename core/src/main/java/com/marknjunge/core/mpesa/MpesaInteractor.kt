package com.marknjunge.core.mpesa

import android.util.Base64
import com.marknjunge.core.BuildConfig
import com.marknjunge.core.model.LNMPaymentResponse
import com.marknjunge.core.model.OAuthAccess
import com.marknjunge.core.model.STKPush
import com.marknjunge.core.utils.Utils
import io.reactivex.Single

interface MpesaInteractor {
    fun sendStkPush(amount: Int, phoneNumber: String, accountRef: String, fcmToken: String): Single<LNMPaymentResponse>
}

class MpesaInteractorImpl : MpesaInteractor {
    private val consumerKey: String = BuildConfig.safaricomConsumerKey
    private val consumerSecret: String = BuildConfig.safaricomConsumerSecret
    private val mpesaService: MpesaService by lazy {
        NetworkProvider().mpesaService
    }

    object Config {
        const val BUSINESS_SHORT_CODE = "174379"
        const val PARTY_B = "174379"
        const val TRANSACTION_TYPE = "CustomerPayBillOnline"
        const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
        const val CALLBACK_URL = "https://us-central1-justjava-android.cloudfunctions.net/callback_url/"
    }

    private fun getAccessToken(): Single<OAuthAccess> {
        val key = "$consumerKey:$consumerSecret"
        val accessTokenHeader = "Basic ${Base64.encodeToString(key.toByteArray(), Base64.NO_WRAP)}"

        return mpesaService.getAccessToken(accessTokenHeader)
    }

    override fun sendStkPush(amount: Int, phoneNumber: String, accountRef: String, fcmToken: String): Single<LNMPaymentResponse> {
        val timestamp = Utils.timestamp
        val sanitizedPhoneNumber = Utils.sanitizePhoneNumber(phoneNumber)
        val password = Utils.getPassword(Config.BUSINESS_SHORT_CODE, Config.PASSKEY, timestamp)

        val stkPush = STKPush(
                Config.BUSINESS_SHORT_CODE,
                password,
                timestamp,
                Config.TRANSACTION_TYPE,
                amount,
                sanitizedPhoneNumber,
                Config.PARTY_B,
                sanitizedPhoneNumber,
                Config.CALLBACK_URL + fcmToken,
                accountRef,
                "Payment for order: $accountRef")

        return getAccessToken()
                .flatMap { oAuthAccess ->
                    val lnmHeader = "Bearer ${oAuthAccess.accessToken}"
                    mpesaService.sendPush(lnmHeader, stkPush)
                }
    }
}