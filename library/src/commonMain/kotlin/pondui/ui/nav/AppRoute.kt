package pondui.ui.nav

import kotlinx.serialization.Serializable

@Serializable
open class AppRoute(
    override val title: String,
) : NavRoute {
    private val titlePath get() = title.lowercase().replace(' ', '-')

    override fun toPath() = titlePath

    fun matchRoute(path: String) = if (path == titlePath) this else null
}

@Serializable
open class IdRoute<T>(
    override val title: String,
    val id: T?
) : NavRoute {
    private val titlePath get() = title.lowercase().replace(' ', '-')

    override fun toPath() = id?.let { "$titlePath/$it" } ?: titlePath
}

fun <T> matchLongIdRoute(path: String, routeTitle: String, toRoute: (Long) -> T): T? =
    path.takeIf { it.startsWith(routeTitle.lowercase()) }?.let {
        val split = path.split('/')
        if (split.size != 2) return@let null
        val id = split[1].toLongOrNull()
        if (id == null) return@let null
        toRoute(id)
    }

fun <T> matchStringIdRoute(path: String, routeTitle: String, toRoute: (String) -> T): T? =
    path.takeIf { it.startsWith(routeTitle.lowercase()) }?.let {
        val split = path.split('/')
        if (split.size != 2) return@let null
        val id = split[1]
        if (id.isEmpty()) return@let null
        toRoute(id)
    }

fun <T> matchStringOrNullIdRoute(path: String, routeTitle: String, toRoute: (String?) -> T): T? =
    path.takeIf { it.startsWith(routeTitle.lowercase()) }?.let {
        val split = path.split('/')
        if (split.size != 2) return@let toRoute(null)
        val id = split[1]
        if (id.isEmpty()) return@let toRoute(null)
        toRoute(id)
    }