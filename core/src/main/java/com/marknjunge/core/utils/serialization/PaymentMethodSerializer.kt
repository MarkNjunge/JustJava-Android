package com.marknjunge.core.utils.serialization

import com.marknjunge.core.data.model.PaymentMethod
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor

@Serializer(forClass = PaymentMethod::class)
object PaymentMethodSerializer {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor

    override fun deserialize(decoder: Decoder): PaymentMethod {
        return PaymentMethod.valueOf(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, obj: PaymentMethod) {
        encoder.encodeString(obj.s)
    }
}