package pondui.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import pondui.ui.behavior.Magic
import pondui.ui.behavior.clickableWithoutHoverEffect
import pondui.ui.controls.Text
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors
import pondui.utils.addShadow
import pondui.utils.lighten

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun CloudPortalView(
    viewModel: CloudPortalModel,
    hazeState: HazeState,
) {
    val state by viewModel.state.collectAsState()

    Magic(state.isDialogVisible) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .background(Pond.colors.background.copy(.5f))
                .padding(Pond.ruler.doublePadding)
                .clickableWithoutHoverEffect(onClick = state.dismissDialog)
        ) {
            Magic(offsetY = 40.dp, rotationY = 90, durationMillis = 1000) {
                val surfaceBook = Pond.colors.surfaceBook
                Box(
                    modifier = Modifier.clip(Pond.ruler.pill)
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.ultraThin(Pond.colors.background.lighten(.1f).copy(.8f))
                        )
                        .drawBehind {
                            drawRoundRect(
                                color = surfaceBook,
                                size = size,
                                cornerRadius = CornerRadius(50f, 50f),
                                style = Stroke(width = 6f)
                            )
                        }
                        .padding(horizontal = Pond.ruler.unitSpacing * 6, vertical = Pond.ruler.unitSpacing)
                ) {
                    Text(state.dialogTitle, Pond.typo.h3.addShadow())
                }
            }
            Spacer(modifier = Modifier.height(Pond.ruler.unitSpacing * 2))
            ProvideBookColors {
                Magic(offsetX = 60.dp) {
                    Box(
                        modifier = Modifier.clickableWithoutHoverEffect { }
                            .shadow(Pond.ruler.shadowElevation, shape = Pond.ruler.bigCorners)
                            .background(Pond.localColors.surface)
                            // .hazeEffect(state = hazeState, style = HazeMaterials.regular(Pond.localColors.surface.darken(.05f)))
                            .padding(Pond.ruler.doublePadding)
                    ) {
                        state.dialogContent()
                    }
                }
            }
        }
    }
}