package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import kotlinx.collections.immutable.ImmutableList
import pondui.ui.nav.NavRoute
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun TabCard(
    initialTab: String = "",
    onChangeTab: ((String) -> Unit)? = null,
    shape: Shape = RectangleShape, // todo: implement shape
    modifier: Modifier = Modifier, // todo: implement modifier
    content: @Composable TabScope.() -> Unit
) {
    ProvideBookColors {
        Tabs(
            selectedTab = initialTab,
            content = content,
            onChangeTab = onChangeTab,
            modifier = modifier
                .shadow(Pond.ruler.shadowElevation, shape)
                .background(Pond.colors.surfaceBook)
                .animateContentSize()
        )
    }
}