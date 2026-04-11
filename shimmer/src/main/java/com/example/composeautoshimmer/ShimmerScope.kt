package com.example.composeautoshimmer

import androidx.compose.foundation.background

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composeautoshimmer.core.ShimmerDefaults

/**
 * Global configuration for shimmer effects.
 * Use this to maintain a consistent look across your application.
 *
 * @property baseColor      The background color of the shimmer skeleton.
 * @property highlightColor The color of the animated shimmer sweep band.
 * @property durationMillis Total time for one sweep animation in milliseconds.
 * @property delayMillis    Wait time between animation cycles.
 * @property angleDegrees   The tilt angle of the shimmer sweep.
 * @property cornerRadius   Default corner radius for shimmer components.
 */
data class ShimmerConfig(
    val baseColor: Color = ShimmerDefaults.BaseColor,
    val highlightColor: Color = ShimmerDefaults.HighlightColor,
    val durationMillis: Int = ShimmerDefaults.DurationMillis,
    val delayMillis: Int = ShimmerDefaults.DelayMillis,
    val angleDegrees: Float = ShimmerDefaults.AngleDegrees,
    val cornerRadius: Dp = ShimmerDefaults.CornerRadiusDp.dp
)

/**
 * [CompositionLocal] used to provide [ShimmerConfig] down the hierarchy.
 */
val LocalShimmerConfig = staticCompositionLocalOf { ShimmerConfig() }

/**
 * [CompositionLocal] used to track if a [ShimmerBox] is currently in the loading state.
 * Useful for conditional styling in custom components.
 */
val LocalShimmerActive = compositionLocalOf { false }

/**
 * A specialized background modifier that becomes transparent when a [ShimmerBox] 
 * is in the loading state. Use this for top-level containers and cards to ensure 
 * the auto-shimmer engine can "see through" to the content inside.
 *
 * @param color The background color to show when NOT shimmering.
 * @param shape The shape of the background.
 */
fun Modifier.shimmerBackground(
    color: Color,
    shape: Shape = RectangleShape
): Modifier = this.then(
    Modifier.composed {
        val active = LocalShimmerActive.current
        if (active) {
            // Match the Theme-level 20% track alpha for consistency
            background(LocalShimmerConfig.current.baseColor.copy(alpha = 0.2f), shape)
        } else {
            background(color, shape)
        }
    }
)

/**
 * A modifier that transforms its content into a solid shimmering shape 
 * (like a circle or square) when a [ShimmerBox] is in the loading state.
 * This is the recommended way to handle Icons in your shimmer layout.
 *
 * @param shape The shape of the skeleton bar/block.
 */
fun Modifier.shimmerPlaceholder(
    shape: Shape = RectangleShape
): Modifier = this.then(
    Modifier.composed {
        val active = LocalShimmerActive.current
        if (active) {
            this.graphicsLayer { alpha = 0f }
                .background(LocalShimmerConfig.current.highlightColor, shape)
        } else {
            this
        }
    }
)

/**
 * Convenience modifier to turn an Icon or component into a shimmering circular block.
 */
fun Modifier.shimmerCircle(): Modifier = shimmerPlaceholder(androidx.compose.foundation.shape.CircleShape)

/**
 * Convenience modifier to turn an Icon or component into a shimmering square/rectangular block.
 *
 * @param cornerRadius The corner radius of the rectangle.
 */
fun Modifier.shimmerSquare(cornerRadius: Dp = 4.dp): Modifier = 
    shimmerPlaceholder(androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius))

/**
 * A wrapper composable that provides [ShimmerConfig] via [LocalShimmerConfig].
 * Components like [ShimmerBox], [ShimmerOverlay], and [ShimmerPlaceholder] 
 * will use these values by default.
 *
 * @param config  The shimmer configuration to provide.
 * @param content The composable hierarchy that will consume the config.
 */
@Composable
fun ShimmerTheme(
    config: ShimmerConfig = ShimmerConfig(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalShimmerConfig provides config) {
        content()
    }
}

