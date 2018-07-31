package com.marknkamau.justjava.utils.mpesa

import android.util.Base64
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.data.models.LNMPaymentResponse
import com.marknkamau.justjava.data.models.OAuthAccess
import com.marknkamau.justjava.data.models.STKPush
import com.marknkamau.justjava.data.network.MpesaService
import com.marknkamau.justjava.utils.Utils
import io.reactivex.Single

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class Mpesa(private val consumerKey: String, private val consumerSecret: String, private val mpesaService: MpesaService) {

    object Config {
        val BUSINESS_SHORT_CODE = "174379"
        val PARTY_B = "174379"
        val TRANSACTION_TYPE = "CustomerPayBillOnline"
        val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
        val CALLBACK_URL = "https://us-central1-justjava-android.cloudfunctions.net/callback_url/"
    }

    private fun getAccessToken(): Single<OAuthAccess> {
        val keys = "$consumerKey:$consumerSecret"
        val accessTokenHeader = "Basic ${Base64.encodeToString(keys.toByteArray(), Base64.NO_WRAP)}"

        return mpesaService.getAccessToken(accessTokenHeader)
    }

    fun sendStkPush(amount: Int, phoneNumber: String, accountRef: String, fcmToken: String): Single<LNMPaymentResponse> {
        val timestamp = Utils.timestampNow
        val stkPush = STKPush(
                Config.BUSINESS_SHORT_CODE,
                Utils.getPassword(Config.BUSINESS_SHORT_CODE, Config.PASSKEY, timestamp),
                Utils.timestampNow,
                Config.TRANSACTION_TYPE,
                amount,
                Utils.sanitizePhoneNumber(phoneNumber),
                Config.PARTY_B,
                Utils.sanitizePhoneNumber(phoneNumber),
                Config.CALLBACK_URL + fcmToken,
                accountRef, //The account reference
                "Payment for order: $accountRef") //The transaction description

        return getAccessToken()
                .flatMap { oAuthAccess ->
                    val lnmHeader = "Bearer ${oAuthAccess.accessToken}"
                    mpesaService.sendPush(lnmHeader, stkPush)
                }

    }
}