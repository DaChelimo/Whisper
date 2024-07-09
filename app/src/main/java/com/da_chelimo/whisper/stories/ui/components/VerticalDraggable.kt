package com.da_chelimo.whisper.stories.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class DragState(val position: Float) { Visible(0f), HiddenDown(1f) }


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDraggable(
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    onHideStory: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    val positionalThreshold = { totalDistance: Float -> totalDistance * 0.1f }
    val velocityThreshold = { with(density) { (100).dp.toPx() } }

    // TODO: Change this to use Saveable
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragState.Visible,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay()
        )
    }

    BackHandler(enabled = state.currentValue == DragState.Visible) {
        coroutineScope.launch {
            state.animateTo(DragState.HiddenDown)
            onHideStory()
        }
    }

    LaunchedEffect(key1 = state.currentValue) {
        if (state.currentValue == DragState.HiddenDown)
            onHideStory()
    }

    Column(
        modifier = modifier
            .onSizeChanged { layoutSize ->
                val bottomPosition = layoutSize.height

                state.updateAnchors(
                    DraggableAnchors {
                        DragState.entries.forEach { anchor ->
                            anchor at bottomPosition * anchor.position
                        }
                    }
                )
            }
    ) {

        Box(
            Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = state
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(state, Orientation.Vertical)
        ) {
            content()
        }
    }
}