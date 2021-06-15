package com.marknjunge.core.utils.serialization

import com.marknjunge.core.data.model.PaymentMethod
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object PaymentMethodSerializer : KSerializer<PaymentMethod>{
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("paymentMethodSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PaymentMethod {
        return PaymentMethod.valueOf(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: PaymentMethod) {
        encoder.encodeString(value.s)
    }

}