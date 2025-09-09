package pondui.utils

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class FileDao<T: Any>(
    val kClass: KClass<T>,
    val folderName: String = kClass.simpleName ?: error("class name not found"),
    private val json: Json = Json { ignoreUnknownKeys = true },
    val provideKey: (T) -> String
) {
    private val path = "${FileKit.filesDir}/file_dao/$folderName"

    private val folder = PlatformFile(path).also { if (!it.exists()) it.createDirectories() }

    @OptIn(InternalSerializationApi::class)
    private val serializer: KSerializer<T> = kClass.serializer()

    private val flow = MutableStateFlow<List<T>>(emptyList())

    val items: StateFlow<List<T>> = flow

    init {
        CoroutineScope(Dispatchers.IO).launch {
            flow.value = readAll()
        }
    }

    suspend fun readAll() = folder.list().mapNotNull { file ->
        try {
            json.decodeFromString(serializer, file.readString())
        } catch (e: Exception) {
            println("couldn't deserialize: ${file.name}")
            file.delete()
            null
        }
    }

    fun flowSingle(predicate: (T) -> Boolean): Flow<T> = flow.mapNotNull { items -> items.firstOrNull(predicate) }

    suspend fun create(item: T) = write(item).also { flow.value += item }

    suspend fun update(item: T) = write(item)
        .also { flow.value = flow.value.map { if (provideKey(it) == provideKey(item)) item else it } }

    suspend fun delete(item: T) = fileOf(item).delete()
        .also { flow.value = flow.value.filterNot { provideKey(it) == provideKey(item) } }

    private suspend fun write(item: T) = fileOf(item).writeString(json.encodeToString(serializer, item))

    private fun fileOf(item: T) = PlatformFile(folder, "${provideKey(item)}.json")
}