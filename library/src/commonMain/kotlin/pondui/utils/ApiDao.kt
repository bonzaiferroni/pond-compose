package pondui.utils

import kabinet.console.LogHandle
import kabinet.console.globalConsole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

class ApiDao<Item : Any, ItemId, NewItem>(
    private val kClass: KClass<Item>,
    private val provideId: (Item) -> ItemId,
    private val apiReadAll: suspend () -> List<Item>?,
    private val apiCreate: suspend (NewItem) -> Item?,
    private val apiUpdate: suspend (Item) -> Boolean?,
    private val apiDelete: suspend (ItemId) -> Boolean?,
    private val console: LogHandle = globalConsole.getHandle("${kClass.simpleName}ApiDao")
) {
    private val flow = MutableStateFlow<List<Item>>(emptyList())
    private val map = mutableMapOf<ItemId, Item>()

    private val className get() = kClass.simpleName ?: error("Class name not found")

    val items: StateFlow<List<Item>> = flow

    init {
        CoroutineScope(Dispatchers.IO).launch {
            var items = apiReadAll()
            while (items == null) {
                console.logError("$className items not found")
                delay(1.seconds)
                items = apiReadAll()
            }
            addItems(items)
        }
    }

    fun flowSingle(predicate: (Item) -> Boolean): Flow<Item> = flow.mapNotNull { items -> items.firstOrNull(predicate) }

    suspend fun create(item: NewItem) = apiCreate(item)?.let { addItem(it) }

    suspend fun update(item: Item) = apiUpdate(item)?.let { if (it) addItem(item) }

    suspend fun delete(itemId: ItemId) = apiDelete(itemId).also { isSuccess ->
        if (isSuccess == true) removeItemById(itemId)
    }

    private fun addItem(item: Item) {
        map[provideId(item)] = item
        flow.value = map.values.toList()
    }

    private fun addItems(items: List<Item>) {
        items.forEach { item ->
            map[provideId(item)] = item
        }
        flow.value = map.values.toList()
    }

    private fun removeItemById(itemId: ItemId) {
        map.remove(itemId)
        flow.value = map.values.toList()
    }
}