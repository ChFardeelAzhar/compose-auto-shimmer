package com.example.composeautoshimmer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
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

