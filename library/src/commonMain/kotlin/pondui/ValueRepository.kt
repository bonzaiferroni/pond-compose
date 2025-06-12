package pondui

import kotlinx.datetime.Instant

interface ValueRepository {
    fun readStringOrNull(key: String): String?
    fun readString(key: String, defaultValue: String = ""): String
    fun writeString(key: String, value: String)

    fun readBooleanOrNull(key: String): Boolean?
    fun readBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun writeBoolean(key: String, value: Boolean)

    fun readInstantOrNull(key: String): Instant?
    fun readInstant(key: String, defaultValue: Long = 0L): Instant
    fun writeInstant(key: String, value: Instant)

    fun readIntOrNull(key: String): Int?
    fun readInt(key: String, defaultValue: Int = 0): Int
    fun writeInt(key: String, value: Int)
}