package com.example.composeautoshimmer.components

import androidx.compose.animation.core.AnimationVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.composeautoshimmer.LocalShimmerConfig
import com.example.composeautoshimmer.core.rememberShimmerState
import kotlin.math.PI
import kotlin.math.tan

/**
 * A fixed-size shimmer placeholder component, useful for building skeleton layouts.
 *
 * @param width             The width of the placeholder.
 * @param height            The height of the placeholder.
 * @param modifier          Modifier applied to the placeholder.
 * @param cornerRadius      Corner radius for the placeholder (defaults to LocalShimmerConfig).
 * @param baseColor         Background color of the placeholder (defaults to LocalShimmerConfig).
 * @param highlightColor    Color of the shimmer sweep band (defaults to LocalShimmerConfig).
 * @param durationMillis    Duration of one full shimmer sweep in milliseconds.
 * @param delayMillis       Pause between shimmer sweep cycles.
 */
@Composable
fun ShimmerPlaceholder(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = LocalShimmerConfig.current.cornerRadius,
    baseColor: Color = LocalShimmerConfig.current.baseColor,
    highlightColor: Color = LocalShimmerConfig.current.highlightColor,
    durationMillis: Int = LocalShimmerConfig.current.durationMillis,
    delayMillis: Int = LocalShimmerConfig.current.delayMillis,
) {
    val shimmerState = rememberShimmerState(durationMillis, delayMillis)
    val progress by shimmerState.progress.asState()
    val angleDegrees = LocalShimmerConfig.current.angleDegrees

    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(baseColor)
            .drawWithContent {
                val w = size.width
                val h = size.height

                val angleRad = (angleDegrees * PI / 180).toFloat()
                val tanAngle = tan(angleRad)
                val bandOffset = tanAngle * h
                val totalTravel = w + bandOffset * 2
                val currentX = (progress * totalTravel) - bandOffset

                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            baseColor,
                            baseColor,
                            highlightColor,
                            baseColor,
                            baseColor
                        ),
                        start = Offset(currentX - bandOffset, 0f),
                        end = Offset(currentX + bandOffset, h)
                    ),
                    size = size
                )
            }
    )
}

/**
 * Extension to convert Animatable float value to Compose State safely.
 */
@Composable
private fun <T, V : AnimationVector> androidx.compose.animation.core.Animatable<T, V>.asState(): State<T> {
    return remember(this) { derivedStateOf { value } }
}

