package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import pondui.ui.behavior.Magic
import pondui.ui.behavior.magic
import pondui.ui.behavior.ifTrue
import pondui.ui.theme.Pond

@Composable
fun Tabs(
    selectedTab: String = "",
    tabs: ImmutableList<Tab>,
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape = Pond.ruler.shroomed,
    modifier: Modifier = Modifier,
) {
    if (tabs.isEmpty()) return
    var currentTab by remember { mutableStateOf(tabs.first()) }
    var indexDelta by remember { mutableStateOf(0) }

    fun changeTab(tabName: String) {
        val startIndex = tabs.indexOf(currentTab)
        val tab = tabs.firstOrNull() { it.name == tabName } ?: tabs.first()
        if (tab == currentTab) return
        val endIndex = tabs.indexOf(tab)
        indexDelta = endIndex - startIndex
        currentTab = tab
        onChangeTab?.invoke(tab.name)
    }

    LaunchedEffect(selectedTab) {
        changeTab(selectedTab)
    }

    Column(
        verticalArrangement = Pond.ruler.columnUnit,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.clip(headerShape)
                .background(Pond.colors.void)
        ) {
            tabs.forEachIndexed { index, tab ->
                if (!tab.isVisible) return@forEachIndexed
                Box(
                    modifier = Modifier.ifTrue(currentTab.name != tab.name) { Modifier.clickable { changeTab(tab.name) } }
                        .weight(1f)
                        .height(IntrinsicSize.Max)
                ) {
                    val offsetX = if (currentTab.name == tab.name) -indexDelta * 100 else indexDelta * 100
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .magic(currentTab.name == tab.name, offsetX = offsetX)
                            .clip(headerShape)
                            .background(Pond.colors.selected)
                    )

                    Text(
                        text = tab.name,
                        modifier = Modifier.align(Alignment.Center)
                            .padding(Pond.ruler.doublePadding)
                            .magic(offsetX = -(index * 10 + 10), durationMillis = index * 300 + 300),
                        maxLines = 1
                    )
                }
            }
        }
        Box {
            for (tab in tabs) {
                if (!tab.isVisible) continue
                val offsetX = if (currentTab.name == tab.name) indexDelta * 100 else -indexDelta * 100
                Magic(tab.name == currentTab.name, offsetX = offsetX) {
                    Column(
                        verticalArrangement = Pond.ruler.columnUnit,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tab.content()
                    }
                }
            }
        }
    }
}

@Composable
fun Tabs(
    selectedTab: String = "",
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape = Pond.ruler.shroomed,
    modifier: Modifier = Modifier,
    tabContent: TabContentBuilder.() -> Unit
) {
    val tabs = remember {
        val builder = TabContentBuilder()
        tabContent(builder)
        builder.tabs.toImmutableList()
    }
    Tabs(
        selectedTab = selectedTab,
        tabs = tabs,
        onChangeTab = onChangeTab,
        headerShape = headerShape,
        modifier = modifier,
    )
}

class TabContentBuilder {
    val tabs = mutableListOf<Tab>()

    fun tab(label: String, content: @Composable () -> Unit,) {
        tabs.add(Tab(label, true, content))
    }
}

data class Tab(
    val name: String,
    val isVisible: Boolean = true,
    val content: @Composable () -> Unit,
)