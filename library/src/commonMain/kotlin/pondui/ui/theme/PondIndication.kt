package pondui.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object LighterHoverIndication : Indication, IndicationNodeFactory {
    override fun create(
        interactionSource: InteractionSource,
    ): Modifier.Node = object : Modifier.Node(), DrawModifierNode {
        private var hovered by mutableStateOf(false)
        private var job: Job? = null
        private val alpha = Animatable(0f)

        override fun onAttach() {
            job = coroutineScope.launch {
                interactionSource.interactions.collect { i ->
                    when (i) {
                        is HoverInteraction.Enter -> {
                            hovered = true
                            alpha.animateTo(
                                targetValue = 0.05f,
                                animationSpec = tween(durationMillis = 140, easing = LinearOutSlowInEasing)
                            )
                        }
                        is HoverInteraction.Exit -> {
                            hovered = false
                            alpha.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 120, easing = FastOutLinearInEasing)
                            )
                        }
                        is PressInteraction.Press -> {
                            alpha.animateTo(
                                0.10f, // brighter when clicked
                                tween(60, easing = LinearOutSlowInEasing)
                            )
                        }
                        is PressInteraction.Release,
                        is PressInteraction.Cancel -> {
                            // return to hover or none
                            val target = if (alpha.value > 0f) 0.05f else 0f
                            alpha.animateTo(
                                target,
                                tween(80, easing = FastOutLinearInEasing)
                            )
                        }
                    }
                }
            }
        }

        override fun onDetach() {
            job?.cancel()
            job = null
            hovered = false
        }

        override fun ContentDrawScope.draw() {
            drawContent()
            val a = alpha.value
            if (a > 0f) drawRect(Color.White.copy(alpha = a))
        }
    }

    override fun equals(other: Any?): Boolean = (other === this)
    override fun hashCode(): Int = this::class.hashCode()
}