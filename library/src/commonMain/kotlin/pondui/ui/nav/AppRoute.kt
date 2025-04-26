package pondui.ui.nav

import kotlinx.serialization.Serializable

@Serializable
open class AppRoute(
    override val title: String,
    val id: Long? = null
) : NavRoute {
    private val titlePath get() = title.lowercase().replace(' ', '-')

    override fun toPath() = id?.let { "$titlePath/$it" } ?: titlePath

    fun matchRoute(path: String) = if (path.startsWith(titlePath)) this else null
}

fun <T> matchIdRoute(path: String, routeTitle: String, toRoute: (Long) -> T): T? =
    path.takeIf { it.startsWith(routeTitle) }?.let {
        val split = path.split('/')
        if (split.size != 2) return@let null
        val id = split[1].toLongOrNull()
        if (id == null) return@let null
        toRoute(id)
    }