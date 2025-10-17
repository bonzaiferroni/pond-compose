package pondui.ui.controls

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import pondui.ui.modifiers.ifTrue
import pondui.ui.modifiers.magicBackground
import pondui.ui.modifiers.pad
import pondui.ui.theme.Pond
import pondui.utils.electrify
import pondui.utils.mixWith

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    isOpen: Boolean = false,
    isEnabled: Boolean = true,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    headerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    var isDrawerOpen by remember(isOpen) { mutableStateOf(isOpen) }

    LaunchedEffect(isEnabled) {
        if (!isEnabled) isDrawerOpen = false
    }

    Drawer(
        isOpen = isDrawerOpen,
        modifier = modifier,
        isEnabled = isEnabled,
        onToggle = { isDrawerOpen = !isDrawerOpen },
        animationSpec = animationSpec,
        headerContent = headerContent,
        content = content
    )
}

@Composable
fun Drawer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessMediumLow),
    color: Color = Pond.colors.secondary.mixWith(Pond.colors.void),
    onToggle: () -> Unit,
    headerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val fraction by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = animationSpec,
        label = "drawerFraction"
    )

    Layout(
        modifier = modifier.clip(Pond.ruler.torpedo)
            .background(Pond.localColors.section),
        contents = listOf(
            {
                val bg = if (isOpen) color.electrify() else color
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(Pond.ruler.unitCorners)
                        .magicBackground(bg)
                        .ifTrue(isEnabled) { actionable(onClick = onToggle) }
                        .pad(2)
                ) {
                    headerContent()
                }
            },
            {
                if (fraction > 0f) {
                    Box(
                        modifier = Modifier.pad(1)
                    ) {
                        content()
                    }
                }
            }                                   // measurables[1]
        )
    ) { (headerMeasurables, contentMeasurables), constraints ->
        val headerPlaceable = headerMeasurables.first().measure(constraints)
        val contentPlaceable = contentMeasurables.takeIf { fraction > 0f }?.first()?.measure(
            constraints.copy(minHeight = 0) // let content be any height
        )

        val width = maxOf(headerPlaceable.width, contentPlaceable?.width ?: 0)
        val headerH = headerPlaceable.height
        val contentH = contentPlaceable?.height ?: 0

        // Visible height: header + revealed portion of content
        val height = (headerH + contentH * fraction).toInt()

        layout(
            width.coerceIn(constraints.minWidth, constraints.maxWidth),
            height.coerceIn(constraints.minHeight, constraints.maxHeight)
        ) {
            // Bottom-align content inside the container.
            // Place so its bottom sits at current container bottom.
            val contentY = (headerH - contentH * (1f - fraction)).toInt()
            contentPlaceable?.placeRelative(0, contentY)

            // Header at top
            headerPlaceable.placeRelative(0, 0)
        }
    }
}