package pondui.ui.behavior

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

// doesn't work
fun Modifier.consumeHover(consume: Boolean): Modifier =
    if (consume) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Enter ||
                        event.type == PointerEventType.Exit  ||
                        event.type == PointerEventType.Move
                    ) {
                        event.changes.forEach { it.consume() }
                    }
                }
            }
        }
    } else this