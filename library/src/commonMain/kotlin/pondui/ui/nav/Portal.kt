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
import pondui.ui.behavior.SlideIn
import pondui.ui.controls.Icon
import pondui.ui.controls.actionable
import pondui.ui.core.PondConfig
import pondui.ui.theme.Pond
import pondui.ui.behavior.ifNotNull
import pondui.ui.controls.Label
import pondui.ui.controls.Row
import pondui.ui.controls.Text
import pondui.utils.darken

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun Portal(
    config: PondConfig,
    exitAction: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    val viewModel: PortalModel = viewModel { PortalModel() }
    val state by viewModel.stateFlow.collectAsState()
    val nav = LocalNav.current
    val navState by nav.state.collectAsState()
    val currentRoute = navState.route
    val hazeState = remember { HazeState() }
    LaunchedEffect(currentRoute) {
        viewModel.setTitle(null)
    }

    CompositionLocalProvider(LocalPortal provides viewModel) {
        Box(
            modifier = Modifier
                .background(Pond.colors.background)
                .fillMaxSize()
        ) {
            val barHeight = portalTopBarHeight

            // glow effect
            Box(
                modifier = Modifier.fillMaxSize()
                    .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin(Pond.colors.background))
            )

            // navigation content
            Box(
                modifier = Modifier.hazeSource(state = hazeState)
            ) {
                content()
            }

            // toasts
            ToastPortalView(viewModel.toastPortalModel)

            // top bar
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.TopStart)
            ) {
                val backRoute = navState.backRoute
                val backAlpha by animateFloatAsState(if (backRoute != null) 1f else .2f)
                val backBackground = Pond.colors.regression
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(barHeight)
                        .shadow(
                            Pond.ruler.shadowElevation,
                            shape = RoundedCornerShape(
                                topEndPercent = 10,
                                bottomStartPercent = 10,
                                bottomEndPercent = 70
                            )
                        )
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.ultraThin(backBackground)
                        )
                        .ifNotNull(backRoute) { actionable(it.title) { nav.goBack() } }
                ) {
                    Icon(
                        imageVector = TablerIcons.ArrowBack,
                        modifier = Modifier.padding(bottom = 5.dp, end = 5.dp)
                            .graphicsLayer { this.alpha = backAlpha }
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(barHeight)
                ) {
                    Row {
                        Text("${config.name} | ", color = Pond.localColors.contentDim)
                        Text(currentRoute.title)
                    }
                }
                // top section
//                Box(
//                    modifier = Modifier.width(IntrinsicSize.Max)
//                        .height(barHeight)
//                        .shadow(
//                            Pond.ruler.shadowElevation,
//                            shape = RoundedCornerShape(
//                                bottomStart = Pond.ruler.bigCorner,
//                                bottomEnd = Pond.ruler.bigCorner
//                            )
//                        )
//                        .hazeEffect(
//                            state = hazeState,
//                            style = HazeMaterials.ultraThin(Pond.colors.void)
//                        )
//                ) {
//                    OldPortalTitle(state.hoverText, state.currentTitle ?: currentRoute.title)
//                }

                if (exitAction != null) {
                    val exitBackground = Pond.colors.negation
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(barHeight)
                            .shadow(
                                Pond.ruler.shadowElevation,
                                shape = RoundedCornerShape(
                                    topStartPercent = 10,
                                    bottomEndPercent = 10,
                                    bottomStartPercent = 70
                                )
                            )
                            .hazeEffect(
                                state = hazeState,
                                style = HazeMaterials.ultraThin(exitBackground)
                            )
                            .actionable("Exit ${config.name}") { exitAction() }
                    ) {
                        Icon(
                            imageVector = TablerIcons.X,
                            modifier = Modifier.padding(bottom = 5.dp, start = 5.dp)
                        )
                    }
                }
            }

            // bottom bar
            SlideIn(
                isVisible = state.bottomBarIsVisible,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .height(portalBottomBarHeight)
                        .shadow(Pond.ruler.shadowElevation, Pond.ruler.shroomUp)
                        .pointerInput(Unit) { }
                        .hazeEffect(state = hazeState, style = HazeMaterials.thin(Pond.colors.void.darken(.1f)))
                ) {
                    BottomBar(portalItems = config.doors)
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

val portalTopBarHeight = 40.dp
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