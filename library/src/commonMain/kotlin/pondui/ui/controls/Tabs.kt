package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import kotlinx.collections.immutable.ImmutableList
import pondui.ui.behavior.FadeIn
import pondui.ui.behavior.fadeIn
import pondui.ui.behavior.modifyIfNotNull
import pondui.ui.behavior.modifyIfTrue
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
            for (tab in tabs) {
                if (!tab.isVisible) continue
                Box(
                    modifier = Modifier.modifyIfTrue(currentTab.name != tab.name) { Modifier.clickable { changeTab(tab.name) } }
                        .weight(1f)
                        .height(IntrinsicSize.Max)
                ) {
                    val offsetX = if (currentTab.name == tab.name) -indexDelta * 100 else indexDelta * 100
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .fadeIn(currentTab.name == tab.name, offsetX = offsetX)
                            .clip(headerShape)
                            .background(Pond.colors.secondary)
                    )

                    Text(
                        text = tab.name,
                        modifier = Modifier.align(Alignment.Center)
                            .padding(Pond.ruler.doublePadding),
                        maxLines = 1
                    )
                }
            }
        }
        Box {
            for (tab in tabs) {
                if (!tab.isVisible) continue
                val offsetX = if (currentTab.name == tab.name) indexDelta * 100 else -indexDelta * 100
                FadeIn(tab.name == currentTab.name, offsetX = offsetX) {
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

data class Tab(
    val name: String,
    val scrollable: Boolean = true,
    val isVisible: Boolean = true,
    val content: @Composable () -> Unit,
)