package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import pondui.ui.modifiers.magic
import pondui.ui.modifiers.ifTrue
import pondui.ui.core.StateScope
import pondui.ui.modifiers.MagicItem
import pondui.ui.theme.Pond
import pondui.utils.darken

@Composable
fun Tabs(
    tabs: ImmutableList<TabItem>,
    modifier: Modifier = Modifier,
    scope: TabScope = remember { TabScope() },
    selectedTab: String? = null,
    tabColor: Color = Pond.colors.selection,
    tabVoidColor: Color = Pond.colors.void.copy(.6f),
    onChangeTab: ((String) -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
) {
    Column(
        gap = 1,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(1, verticalAlignment = Alignment.CenterVertically) {
            TabHeader(
                selectedTab = selectedTab,
                tabColor = tabColor,
                tabVoidColor = tabVoidColor,
                onChangeTab = onChangeTab,
                headerShape = headerShape,
                scope = scope,
                tabs = tabs,
                modifier = Modifier.weight(1f)
            )
            headerContent?.invoke()
        }
        TabContent(
            scope = scope,
        )
    }
}

@Composable
fun Tabs(
    modifier: Modifier = Modifier,
    selectedTab: String? = null,
    tabColor: Color = Pond.colors.selection,
    tabVoidColor: Color = Pond.colors.void.copy(.6f),
    onChangeTab: ((String) -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
    content: @Composable TabContentScope.() -> Unit
) {
    val scope = remember { TabScope() }
    val contentScope = remember { TabContentScope(scope) }
    contentScope.content()
    val tabs = contentScope.toTabs()
    Tabs(
        tabs = tabs,
        scope = scope,
        selectedTab = selectedTab,
        tabColor = tabColor,
        tabVoidColor = tabVoidColor,
        onChangeTab = onChangeTab,
        headerContent = headerContent,
        headerShape = headerShape,
        modifier = modifier,
    )
}

@Composable
fun <T> Tabs(
    items: List<T>,
    modifier: Modifier = Modifier,
    selectedTab: String? = null,
    tabColor: Color = Pond.colors.selection,
    tabVoidColor: Color = Pond.colors.void.copy(.6f),
    onChangeTab: ((String) -> Unit)? = null,
    headerContent: (@Composable () -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
    provideItem: @Composable (Int, T) -> TabItem
) {
    val tabs = items.mapIndexed { index, item -> provideItem(index, item) }.toImmutableList()
    Tabs(
        tabs = tabs,
        selectedTab = selectedTab,
        tabColor = tabColor,
        tabVoidColor = tabVoidColor,
        onChangeTab = onChangeTab,
        headerContent = headerContent,
        headerShape = headerShape,
        modifier = modifier,
    )
}

@Composable
fun TabHeader(
    scope: TabScope,
    tabs: ImmutableList<TabItem>,
    modifier: Modifier = Modifier,
    tabColor: Color = Pond.colors.selection,
    tabVoidColor: Color = Pond.colors.void.copy(.6f),
    selectedTab: String? = null,
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
) {
    scope.initialize(tabs)
    val state by scope.state.collectAsState()
    val indexDelta = state.indexDelta

    val visibleTab = state.visibleTab
    LaunchedEffect(visibleTab) {
        visibleTab?.let { onChangeTab?.invoke(it.label) }
    }

    Row(
        modifier = modifier.clip(headerShape)
            .background(tabVoidColor)
    ) {
        tabs.forEachIndexed { index, tab ->
            if (!tab.isVisible) return@forEachIndexed
            val isSelected = index == state.tabIndex
            Box(
                modifier = Modifier.ifTrue(!isSelected) {
                    clip(headerShape)
                        .actionable(icon = PointerIcon.Hand) { scope.changeTab(tab, index) }
                }
                    .weight(1f)
                    .height(IntrinsicSize.Max)
            ) {
                val distance = indexDelta * (200 / tabs.size)
                val offsetX = if (isSelected) -distance else distance
                Box(
                    modifier = Modifier.fillMaxSize()
                        .magic(isSelected, offsetX = offsetX.dp)
                        .clip(headerShape)
                        .background(tabColor)
                )

                val color = if (isSelected) Pond.colors.contentSky else Pond.colors.contentSky.darken()

                Text(
                    text = tab.label,
                    color = color,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(Pond.ruler.doublePadding),
                    maxLines = 1
                )
            }
        }
    }

    LaunchedEffect(selectedTab) {
        val tabIndex = tabs.indexOfFirst { it.label == selectedTab }
            .takeIf { it >= 0 && selectedTab != visibleTab?.label }
        tabIndex?.let {
            val tab = tabs[tabIndex]
            scope.changeTab(tab, tabIndex)
        }
    }
}

@Composable
fun TabContent(
    scope: TabScope,
    modifier: Modifier = Modifier,
) {
    val state by scope.state.collectAsState()
    Box(modifier = modifier) {
        MagicItem(state.visibleTab, offsetX = (state.indexDelta * 100).dp, isVisibleInit = true) { tab ->
            tab?.content()
        }
    }
}

@Stable
class TabScope() : StateScope<TabState>(TabState()) {
    fun initialize(tabs: List<TabItem>) {
        val visibleTab = stateNow.visibleTab
        if (tabs.isEmpty()) {
            if (visibleTab != null)
                setState { it.copy(visibleTab = null, tabIndex = 0) }
            return
        }

        if (visibleTab != null) {
            val indexTab = tabs.getOrNull(stateNow.tabIndex)
            if (indexTab?.key == visibleTab.key) return
            val nextIndex = minOf(stateNow.tabIndex, tabs.size - 1)
            val nextTab = tabs[nextIndex]
            changeTab(nextTab, nextIndex)
        } else {
            val index = tabs.indexOfFirst { it.isVisible }.takeIf { it >= 0 } ?: return
            val tab = tabs[index]
            changeTab(tab, index)
        }
    }

    fun changeTab(tab: TabItem, tabIndex: Int) {
        setState { it.copy(tabIndex = tabIndex, indexDelta = tabIndex - stateNow.tabIndex, visibleTab = tab) }
    }

    fun setTopPadding(padding: Dp) {
        setState { it.copy(topPadding = padding) }
    }
}

class TabContentScope(
    val tabScope: TabScope
) {
    private val _items = mutableListOf<TabItem>()

    @Composable
    fun Tab(
        label: String,
        isVisible: Boolean = true,
        key: String = label,
        content: @Composable () -> Unit
    ) {
        if (_items.any { it.label == label}) return
        val item = TabItem(label, isVisible, key, content)
        _items.add(item)
    }

    fun toTabs() = _items.toImmutableList()
}

data class TabState(
    val visibleTab: TabItem? = null,
    val indexDelta: Int = 0,
    val topPadding: Dp = 0.dp,
    val tabIndex: Int = 0,
)

data class TabItem(
    val label: String,
    val isVisible: Boolean = true,
    val key: String = label,
    val content: @Composable () -> Unit
)