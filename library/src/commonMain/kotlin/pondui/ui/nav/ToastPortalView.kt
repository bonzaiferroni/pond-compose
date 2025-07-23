package pondui.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import pondui.ui.behavior.magic
import pondui.ui.controls.BottomBarSpacer
import pondui.ui.controls.Text
import pondui.ui.theme.Pond
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun ToastPortalView(
    viewModel: ToastPortalModel
) {
    val state by viewModel.stateFlow.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(Pond.ruler.unitSpacing, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        for (toast in state.toasts) {
            val now = Clock.System.now()
            var isVisible by remember(toast.time) {
                mutableStateOf(now - toast.time < toastDuration)
            }

            LaunchedEffect(toast.time) {
                delay(toastDuration - (toast.time - now))
                isVisible = false
            }

            Text(
                text = toast.content,
                modifier = Modifier.clip(Pond.ruler.unitCorners)
                    .magic(isVisible, scale = .8f)
                    .background(toast.type.toColor())
                    .padding(Pond.ruler.unitPadding)
            )
        }

        BottomBarSpacer()
    }
}

private val toastDuration = 5.seconds

@Composable
fun ToastType.toColor() = when (this) {
    ToastType.Default -> Pond.colors.tertiary
    ToastType.Error -> Pond.colors.danger
}