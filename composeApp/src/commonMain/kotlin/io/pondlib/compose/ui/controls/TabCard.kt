package io.pondlib.compose.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import io.pondlib.compose.ui.nav.NavRoute
import io.pondlib.compose.ui.theme.Pond
import io.pondlib.compose.ui.theme.ProvideBookColors

@Composable
fun TabCard(
    initialTab: String? = null,
    modifyRoute: ((String) -> NavRoute)? = null,
    shape: Shape = RectangleShape, // todo: implement shape
    modifier: Modifier = Modifier, // todo: implement modifier
    content: @Composable TabScope.() -> Unit
) {
    ProvideBookColors {
        Tabs(
            initialTab = initialTab,
            modifyRoute = modifyRoute,
            content = content,
            modifier = modifier
                .shadow(Pond.ruler.shadowElevation, shape)
                .background(Pond.colors.surfaceBook)
                .animateContentSize()
        )
    }
}