package com.example.composenavtypes

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Parcelize
@Serializable(with = ProtectedStringSerializer::class)
data class ProtectedString(val value: String): Parcelable {
    override fun toString() = "[redacted]"
}

object ProtectedStringSerializer : KSerializer<ProtectedString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ProtectedString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ProtectedString) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ProtectedString {
        return ProtectedString(decoder.decodeString())
    }
}

inline fun <reified T : Any?> serializableType(
    nullable: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = nullable) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

inline fun <reified T> serializableTypeMapping(nullable: Boolean) =
    typeOf<T>() to serializableType<T>(nullable = nullable)