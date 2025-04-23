package pondui.ui.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowBack
import compose.icons.tablericons.X
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import pondui.utils.darken
import pondui.ui.behavior.SlideIn
import pondui.ui.controls.IconButton
import pondui.ui.core.PondConfig
import pondui.ui.theme.Pond

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun Portal(
    config: PondConfig,
    exitAction: (() -> Unit)?,
    viewModel: PortalModel = viewModel { PortalModel() },
    content: @Composable () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val nav = LocalNav.current
    val navState by nav.state.collectAsState()
    val currentRoute = navState.route
    val hazeState = remember { HazeState() }

    CompositionLocalProvider(LocalPortal provides viewModel) {
        Box(
            modifier = Modifier.Companion
                .background(Pond.colors.background)
                .fillMaxSize()
        ) {
            val barHeight = 50.dp
            val hazeBackground = Pond.colors.background.darken(-.1f)
            Box(
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .padding(
                        top = 0.dp,
                        start = Pond.ruler.innerSpacing,
                        end = Pond.ruler.innerSpacing,
                        bottom = 0.dp,
                    )
            ) {
                content()
            }

            Row(
                horizontalArrangement = Pond.ruler.rowTight,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .shadow(Pond.ruler.shadowElevation)
                    .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin(hazeBackground))
                    .height(barHeight)
                    .padding(Pond.ruler.innerPadding)
            ) {
                val backRoute = navState.backRoute
                val backAlpha by animateFloatAsState(if (backRoute != null) 1f else 0f)
                val backRouteTitle = backRoute?.title ?: ""
                IconButton(
                    imageVector = TablerIcons.ArrowBack,
                    hoverText = backRouteTitle,
                    modifier = Modifier
                        .graphicsLayer { this.alpha = backAlpha }
                ) { nav.goBack() }

                PortalTitle(state.hoverText, currentRoute)

                if (exitAction != null) {
                    Spacer(modifier = Modifier.width(0.dp))
                    IconButton(TablerIcons.X) { exitAction() }
                }
            }

            SlideIn(
                isVisible = state.bottomBarIsVisible,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .height(portalBottomBarHeight)
                        .shadow(
                            Pond.ruler.shadowElevation, RoundedCornerShape(
                                topStartPercent = 50, topEndPercent = 50,
                                bottomStartPercent = 0, bottomEndPercent = 0
                            )
                        )
                        .pointerInput(Unit) { }
                        .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin(hazeBackground))
                        .padding(horizontal = Pond.ruler.innerSpacing)
                ) {
                    PortalBarControls(portalItems = config.doors)
                }
            }
        }
    }
}

sealed class PortalItem {
    abstract val icon: ImageVector
    abstract val label: String
}

data class PortalAction(
    override val icon: ImageVector,
    override val label: String,
    val action: (Nav) -> Unit
) : PortalItem()

data class PortalDoor(
    override val icon: ImageVector,
    val route: NavRoute,
    override val label: String = route.title,
    val requireLogin: Boolean = false,
) : PortalItem()

val gradientColorList = listOf(
    Color.Transparent,
    Color.Transparent,
    Color.White.copy(alpha = .1f),
    Color.White.copy(alpha = .1f),
    Color.Transparent,
    Color.Transparent,
    Color.White.copy(alpha = .2f),
    Color.Transparent,
)

val LocalPortal = staticCompositionLocalOf<PortalModel> {
    error("No portal provided")
}

val portalTopBarHeight = 50.dp
val portalBottomBarHeight = 70.dp

//            .drawBehind {
//                drawRect(
//                    brush = Brush.linearGradient(
//                        colors = gradientColorList,
//                        start = Offset(offsetX, 0f),
//                        end = Offset(offsetX + width, 0f),
//                        tileMode = TileMode.Repeated
//                    )
//                )
//                drawRect(
//                    brush = Brush.linearGradient(
//                        colors = gradientColorList,
//                        start = Offset(-offsetX, 0f),
//                        end = Offset(-offsetX - width, 0f),
//                        tileMode = TileMode.Repeated
//                    )
//                )
//            }
//

//val infiniteTransition = rememberInfiniteTransition()
//val width = 2000f
//val offsetX by infiniteTransition.animateFloat(
//    initialValue = width,
//    targetValue = 0f, // Adjust for smooth looping
//    animationSpec = infiniteRepeatable(
//        animation = tween(durationMillis = (width * 20).toInt(), easing = LinearEasing),
//        repeatMode = RepeatMode.Restart
//    )
//)