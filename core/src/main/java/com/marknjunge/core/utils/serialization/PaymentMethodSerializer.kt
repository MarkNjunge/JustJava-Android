package com.marknjunge.core.utils.serialization

import com.marknjunge.core.data.model.PaymentMethod
import kotlinx.serialization.*

@Serializer(forClass = PaymentMethod::class)
object PaymentMethodSerializer {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("paymentMethodSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PaymentMethod {
        return PaymentMethod.valueOf(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, obj: PaymentMethod) {
        encoder.encodeString(obj.s)
    }
}
