package pondui.utils

class Broadcaster<T> {
    private val listeners = mutableListOf<(T) -> Boolean>()

    fun register(listener: (T) -> Boolean) = listeners.add(listener)
    fun unregister(listener: (T) -> Boolean) = listeners.remove(listener)

    /**
     * Broadcasts [value] to all listeners in order.
     * Stops at the first listener that returns true (i.e. “consumes” it).
     * Returns true if any listener consumed the value.
     */
    fun broadcast(value: T): Boolean = listeners.any { it(value) }
}