package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.utils.modifyIfNotNull
import pondui.utils.modifyIfTrue
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.ui.theme.Pond

@Composable
fun Tabs(
    initialTab: String? = null,
    modifyRoute: ((String) -> NavRoute)? = null,
    headerShape: Shape = Pond.ruler.shroomed,
    modifier: Modifier = Modifier,
    viewModel: TabsModel = viewModel { TabsModel(initialTab) },
    content: @Composable TabScope.() -> Unit
) {
    val state by viewModel.state.collectAsState()
    val nav = LocalNav.current

    fun onTabChange(tab: String) {
        modifyRoute?.let {
            val navRoute = it(tab)
            nav.setRoute(navRoute)
        }
        viewModel.setTab(tab)
    }

    val scope = TabScope()
    scope.content()
    val tabs: List<TabItem> = scope.tabs
    if (tabs.isEmpty()) return

    val currentTab = tabs.firstOrNull() { it.name == state.tab }

    if (currentTab == null) {
        onTabChange(tabs.first().name)
        return
    }

    Column(
        verticalArrangement = Pond.ruler.columnUnit,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.clip(headerShape)
                .background(Pond.colors.void)
        ) {
            for (tab in tabs) {
                if (!tab.isVisible) continue
                val background = when {
                    currentTab.name == tab.name -> Pond.colors.secondary
                    else -> Color.Transparent
                }
                Box(
                    modifier = Modifier.clip(headerShape)
                        .modifyIfTrue(currentTab.name != tab.name) { Modifier.clickable { onTabChange(tab.name) } }
                        .background(background)
                        .padding(Pond.ruler.halfPadding)
                        .weight(1f)
                ) {
                    Text(
                        text = tab.name,
                        modifier = Modifier.align(Alignment.Center),
                        maxLines = 1
                    )
                }
            }
        }
        val scrollState = when {
            currentTab.scrollable -> rememberScrollState()
            else -> null
        }
        Column(
            verticalArrangement = Pond.ruler.columnUnit,
            modifier = Modifier.fillMaxWidth()
                .clip(Pond.ruler.rounded)
                .modifyIfNotNull(scrollState) { verticalScroll(it) }
        ) {
            currentTab.content()
        }
    }
}

@Composable
fun TabScope.Tab(
    name: String,
    scrollable: Boolean = true,
    isVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    this.tabs.add(TabItem(
        name = name,
        scrollable = scrollable,
        isVisible = isVisible,
        content = content
    ))
}

internal data class TabItem(
    val name: String,
    val scrollable: Boolean = true,
    val isVisible: Boolean = true,
    val content: @Composable () -> Unit,
)

@Stable
class TabScope internal constructor() {
    internal val tabs = mutableListOf<TabItem>()
}