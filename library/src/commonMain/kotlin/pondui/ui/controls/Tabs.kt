package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.magic
import pondui.ui.modifiers.ifTrue
import pondui.ui.core.StateScope
import pondui.ui.theme.Pond
import pondui.utils.darken

@Composable
fun Tabs(
    selectedTab: String? = null,
    modifier: Modifier = Modifier,
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
    content: @Composable TabScope.() -> Unit
) {
    val scope = remember { TabScope() }

    Column(
        verticalArrangement = Pond.ruler.columnUnit,
        modifier = modifier.fillMaxWidth()
    ) {
        TabHeader(
            selectedTab = selectedTab,
            onChangeTab = onChangeTab,
            headerShape = headerShape,
            scope = scope,
        )
        TabContent(
            scope = scope,
            content = content,
        )
    }
}

@Composable
fun TabHeader(
    scope: TabScope,
    modifier: Modifier = Modifier,
    selectedTab: String? = null,
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape = Pond.ruler.pillTopRoundedBottom,
) {
    val state by scope.state.collectAsState()
    val items = state.items
    val currentLabel = state.currentLabel
    val indexDelta = state.indexDelta

    LaunchedEffect(state.currentLabel) {
        if (state.currentLabel.isNotEmpty()) {
            onChangeTab?.invoke(state.currentLabel)
        }
    }

    Row(
        modifier = modifier.clip(headerShape)
            .background(Pond.colors.void)
    ) {
        items.forEachIndexed { index, tab ->
            if (!tab.isVisible) return@forEachIndexed
            val isSelected = currentLabel == tab.label
            Box(
                modifier = Modifier.ifTrue(!isSelected) {
                    clip(headerShape)
                        .actionable(icon = PointerIcon.Hand) { scope.changeTab(tab.label) }
                }
                    .weight(1f)
                    .height(IntrinsicSize.Max)
            ) {
                val offsetX = if (isSelected) -indexDelta * 100 else indexDelta * 100
                Box(
                    modifier = Modifier.fillMaxSize()
                        .magic(isSelected, offsetX = offsetX.dp)
                        .clip(headerShape)
                        .background(Pond.colors.selection)
                )

                val color = if (isSelected) Pond.colors.contentSky else Pond.colors.contentSky.darken()

                Text(
                    text = tab.label,
                    color = color,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(Pond.ruler.doublePadding)
                        .magic(offsetX = (-index * 10 + 10).dp, durationMillis = index * 300 + 300),
                    maxLines = 1
                )
            }
        }
    }

    LaunchedEffect(selectedTab) {
        if (selectedTab != null) scope.changeTab(selectedTab)
    }
}

@Composable
fun TabContent(
    scope: TabScope,
    modifier: Modifier = Modifier,
    content: @Composable TabScope.() -> Unit
) {
    Box(modifier = modifier) {
        scope.content()
    }
}

@Composable
fun TabScope.Tab(
    label: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    val state by this@Tab.state.collectAsState()

    addItem(label, isVisible)

    LaunchedEffect(isVisible) {
        this@Tab.changeVisibility(label, isVisible)
    }

    val offsetX = if (state.currentLabel == label) state.indexDelta * 100 else -state.indexDelta * 100
    Magic(label == state.currentLabel, offsetX = offsetX.dp) {
        Column(
            gap = 1,
            modifier = modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Stable
class TabScope : StateScope<TabState>(TabState()) {

    fun addItem(label: String, isVisible: Boolean) {
        if (stateNow.items.any { it.label == label }) return
        val item = TabItem(label, isVisible)
        val currentLabel = stateNow.currentLabel.takeIf { it.isNotEmpty() } ?: item.label
        setState { it.copy(items = it.items + item, currentLabel = currentLabel) }
    }

    fun changeVisibility(label: String, isVisible: Boolean) {
        if (stateNow.items.all { it.label != label || it.isVisible == isVisible }) return
        val items = stateNow.items.map { if (it.label == label) it.copy(isVisible = isVisible) else it }
        setState { it.copy(items = items) }
    }

    fun changeTab(tabName: String) {
        val currentLabel = stateNow.currentLabel
        val items = stateNow.items
        val startIndex = items.indexOfFirst { it.label == currentLabel }.takeIf { it != -1 } ?: return
        val item = items.firstOrNull() { it.label == tabName } ?: items.first { it.isVisible }
        if (item.label == currentLabel) return
        val endIndex = items.indexOfFirst { it.label == tabName }
        setState { it.copy(indexDelta = endIndex - startIndex, currentLabel = item.label) }
    }
}

data class TabState(
    val currentLabel: String = "",
    val indexDelta: Int = 0,
    val items: List<TabItem> = emptyList(),
)

data class TabItem(
    val label: String,
    val isVisible: Boolean
)