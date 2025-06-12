package pondui

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

class LocalValueRepository(): ValueRepository {

    companion object {
        val settings = Settings()
    }

    override fun readStringOrNull(key: String) = settings.getStringOrNull(key)
    override fun readString(key: String, defaultValue: String) = settings.getString(key, defaultValue)
    override fun writeString(key: String, value: String) = settings.putString(key, value)

    override fun readBooleanOrNull(key: String) = settings.getBooleanOrNull(key)
    override fun readBoolean(key: String, defaultValue: Boolean) = settings.getBoolean(key, defaultValue)
    override fun writeBoolean(key: String, value: Boolean) = settings.putBoolean(key, value)

    override fun readInstantOrNull(key: String) = settings.getLongOrNull(key)?.let { Instant.fromEpochMilliseconds(it) }
    override fun readInstant(key: String, defaultValue: Long) = settings.getLong(key, defaultValue).let { Instant.fromEpochMilliseconds(it) }
    override fun writeInstant(key: String, value: Instant) = settings.set(key, value.toEpochMilliseconds())

    override fun readIntOrNull(key: String) = settings.getIntOrNull(key)
    override fun readInt(key: String, defaultValue: Int) = settings.getInt(key, defaultValue)
    override fun writeInt(key: String, value: Int) = settings.putInt(key, value)

    inline fun <reified T> readObjectOrNull(): T? = T::class.simpleName?.let { className ->
        readObjectOrNull(className)
    }

    inline fun <reified T> readObjectOrNull(key: String): T? = settings.getStringOrNull(key)?.let {
        Json.Default.decodeFromString(it)
    }

    inline fun <reified T> writeObject(value: T) {
        val className = T::class.simpleName ?: error("Must use type with a name")
        writeObject(className, value)
    }

    inline fun <reified T> writeObject(key: String, value: T) {
        settings.putString(key, Json.Default.encodeToString(value))
    }
}