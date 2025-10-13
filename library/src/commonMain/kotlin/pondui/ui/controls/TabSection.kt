package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond

@Composable
fun TabSection(
    modifier: Modifier = Modifier,
    selectedTab: String? = null,
    background: Color = Pond.localColors.sectionSurface,
    tabColor: Color = Pond.colors.selection,
    onChangeTab: ((String) -> Unit)? = null,
    headerShape: Shape? = null,
    content: @Composable TabScope.() -> Unit
) {
    val scope = remember { TabScope() }

    val unitDp = Pond.ruler.unitSpacing
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.clip(headerShape ?: Pond.ruler.pillTop)
                .background(background)
                .padding(start = unitDp, top = unitDp, end = unitDp, bottom = 0.dp )
        ) {
            TabHeader(
                selectedTab = selectedTab,
                onChangeTab = onChangeTab,
                tabColor = tabColor,
                headerShape = headerShape ?: Pond.ruler.pillTopRoundedBottom,
                scope = scope,
            )
        }
        Box(
            modifier = Modifier.clip(Pond.ruler.squareTopRoundedBottom)
                .background(background)
                .padding(unitDp)
        ) {
            TabContent(
                scope = scope,
                content = content,
            )
        }
    }
}