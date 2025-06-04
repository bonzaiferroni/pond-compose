package pondui.ui.controls

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import kotlinx.coroutines.flow.collectLatest

//object ScaleIndication : Indication {
//    @Composable
//    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
//        // key the remember against interactionSource, so if it changes we create a new instance
//        val instance = remember(interactionSource) { ScaleIndicationInstance() }
//
//        LaunchedEffect(interactionSource) {
//            interactionSource.interactions.collectLatest { interaction ->
//                when (interaction) {
//                    is PressInteraction.Press -> instance.animateToPressed(interaction.pressPosition)
//                    is PressInteraction.Release -> instance.animateToResting()
//                    is PressInteraction.Cancel -> instance.animateToResting()
//                }
//            }
//        }
//
//        return instance
//    }
//}

//private class ScaleIndicationInstance : IndicationInstance {
//    var currentPressPosition: Offset = Offset.Zero
//    val animatedScalePercent = Animatable(1f)
//
//    suspend fun animateToPressed(pressPosition: Offset) {
//        currentPressPosition = pressPosition
//        animatedScalePercent.animateTo(0.9f, spring())
//    }
//
//    suspend fun animateToResting() {
//        animatedScalePercent.animateTo(1f, spring())
//    }
//
//    override fun ContentDrawScope.drawIndication() {
//        scale(
//            scale = animatedScalePercent.value,
//            pivot = currentPressPosition
//        ) {
//            this@drawIndication.drawContent()
//        }
//    }
//}