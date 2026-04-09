package com.example.composeautoshimmer.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Holds and manages the shimmer animation state.
 * Uses [Animatable] to drive a 0f → 1f progress value
 * that components use to translate the shimmer highlight band.
 *
 * @param coroutineScope Scope used to launch the infinite animation
 */
@Stable
class ShimmerState(
    private val coroutineScope: CoroutineScope
) {
    /** Animation progress — 0f (start) to 1f (end of sweep) */
    val progress = Animatable(0f)

    /**
     * Starts the infinite shimmer sweep animation.
     * Called automatically when shimmer enters composition.
     */
    fun start(
        durationMillis: Int = ShimmerDefaults.DurationMillis,
        delayMillis: Int = ShimmerDefaults.DelayMillis
    ) {
        coroutineScope.launch {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart,
                    initialStartOffset = StartOffset(delayMillis)
                )
            )
        }
    }

    /**
     * Stops the shimmer animation and resets progress to 0.
     */
    fun stop() {
        coroutineScope.launch {
            progress.stop()
            progress.snapTo(0f)
        }
    }
}

/**
 * Creates and remembers a [ShimmerState] instance tied to the composition lifecycle.
 * Animation starts automatically on first composition.
 */
@Composable
fun rememberShimmerState(
    durationMillis: Int = ShimmerDefaults.DurationMillis,
    delayMillis: Int = ShimmerDefaults.DelayMillis
): ShimmerState {
    val scope = rememberCoroutineScope()
    return remember {
        ShimmerState(scope).also {
            it.start(durationMillis, delayMillis)
        }
    }
}