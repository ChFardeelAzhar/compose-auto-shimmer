package com.example.composeautoshimmer.components

import androidx.compose.animation.core.AnimationVector
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.example.composeautoshimmer.LocalShimmerConfig
import com.example.composeautoshimmer.core.rememberShimmerState
import kotlin.math.PI
import kotlin.math.tan

/**
 * A wrapper composable that adds a shimmer sweep effect on top of existing content.
 * Unlike [ShimmerBox], the underlying content remains fully visible.
 *
 * @param active            When true, the shimmer sweep animation is active.
 * @param modifier          Modifier applied to the outer container.
 * @param color             The color of the bright shimmer band (defaults to highlightColor with alpha).
 * @param durationMillis    Duration of one full shimmer sweep in milliseconds.
 * @param delayMillis       Pause between shimmer sweep cycles.
 * @param angleDegrees      Angle of the shimmer sweep band.
 * @param content           The UI content to be overlaid with shimmer.
 */
@Composable
fun ShimmerOverlay(
    active: Boolean,
    modifier: Modifier = Modifier,
    color: Color = LocalShimmerConfig.current.highlightColor.copy(alpha = 0.6f),
    durationMillis: Int = LocalShimmerConfig.current.durationMillis,
    delayMillis: Int = LocalShimmerConfig.current.delayMillis,
    angleDegrees: Float = LocalShimmerConfig.current.angleDegrees,
    content: @Composable () -> Unit
) {
    val shimmerState = rememberShimmerState(durationMillis, delayMillis)
    val progress by shimmerState.progress.asState()

    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                contentSize = coordinates.size
            }
            .then(
                if (active && contentSize != IntSize.Zero) {
                    Modifier.drawWithContent {
                        drawContent()

                        val width = contentSize.width.toFloat()
                        val height = contentSize.height.toFloat()

                        val angleRad = (angleDegrees * PI / 180).toFloat()
                        val tanAngle = tan(angleRad)
                        val bandOffset = tanAngle * height
                        val totalTravel = width + bandOffset * 2
                        val currentX = (progress * totalTravel) - bandOffset

                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    color,
                                    Color.Transparent,
                                    Color.Transparent
                                ),
                                start = Offset(currentX - bandOffset, 0f),
                                end = Offset(currentX + bandOffset, height)
                            ),
                            size = size
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

/**
 * Extension to convert Animatable float value to Compose State safely.
 */
@Composable
private fun <T, V : AnimationVector> androidx.compose.animation.core.Animatable<T, V>.asState(): State<T> {
    return remember(this) { derivedStateOf { value } }
}

