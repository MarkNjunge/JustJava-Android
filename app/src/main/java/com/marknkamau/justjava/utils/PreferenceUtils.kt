package com.marknkamau.justjava.utils

import android.content.SharedPreferences
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException

class PreferenceUtils(val preferences: SharedPreferences, val json: Json) {
    fun set(key: String, value: Any) {
        when (value) {
            is String -> edit { putString(key, value) }
            is Int -> edit { putInt(key, value) }
            is Boolean -> edit { putBoolean(key, value) }
            is Float -> edit { putFloat(key, value) }
            is Long -> edit { putLong(key, value) }
            else -> throw UnsupportedOperationException("Saving of ${value.javaClass.name} not yet implemented")
        }
    }

    inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T {
        return preferences.run {
            when (T::class) {
                String::class -> getString(key, defaultValue as? String) as T
                Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
                Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
                Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
                Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
                else -> throw UnsupportedOperationException("Retrieving of ${T::class.java.name} not yet implemented")
            }
        }
    }

    inline fun <reified T> getObject(key: String, serializer: DeserializationStrategy<T>): T? {
        return try {
            val s: String = get(key, "")
            json.parse(serializer, s)
        } catch (e: JsonDecodingException) {
            // If it failed to parse the json, it means the value is null
            null
        }
    }

    inline fun <reified T> setObject(key: String, value: T?, serializer: SerializationStrategy<T>) {
        if (value == null) {
            set(key, "")
        } else {
            val s = json.stringify(serializer, value)
            set(key, s)
        }
    }

    private fun edit(block: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(block).apply()
    }
}
