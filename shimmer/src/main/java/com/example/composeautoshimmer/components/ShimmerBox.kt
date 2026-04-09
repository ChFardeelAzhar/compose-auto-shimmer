package com.example.composeautoshimmer.components

import androidx.compose.animation.core.AnimationVector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.composeautoshimmer.LocalShimmerConfig
import com.example.composeautoshimmer.core.rememberShimmerState
import kotlin.math.PI
import kotlin.math.tan

/**
 * A wrapper composable that automatically renders a pixel-perfect shimmer silhouette
 * of its content. It detects the shapes of text, images, and containers inside it.
 *
 * @param isLoading         When true, content is hidden and replaced by a shimmer silhouette.
 * @param modifier          Modifier applied to the outer container.
 * @param baseColor         The color of the skeleton silhouette (defaults to LocalShimmerConfig).
 * @param highlightColor    The color of the bright shimmer band (defaults to LocalShimmerConfig).
 * @param durationMillis    Duration of one full shimmer sweep (defaults to LocalShimmerConfig).
 * @param delayMillis       Pause between shimmer cycles.
 * @param angleDegrees      Angle of the shimmer sweep band.
 * @param content           The real UI content — automatically silhouetted when loading.
 */
@Composable
fun ShimmerBox(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    baseColor: Color = LocalShimmerConfig.current.baseColor,
    highlightColor: Color = LocalShimmerConfig.current.highlightColor,
    durationMillis: Int = LocalShimmerConfig.current.durationMillis,
    delayMillis: Int = LocalShimmerConfig.current.delayMillis,
    angleDegrees: Float = LocalShimmerConfig.current.angleDegrees,
    content: @Composable () -> Unit
) {
    val shimmerState = rememberShimmerState(durationMillis, delayMillis)
    val progress by shimmerState.progress.asState()

    var contentSize by remember { mutableStateOf(IntSize.Zero) }

    // If we are in loading state, we override the MaterialTheme colors
    // to make backgrounds transparent. This allows our silhouette engine
    // to "see through" Cards/Surfaces to find the text and icons inside.
    val currentColorScheme = MaterialTheme.colorScheme
    val skeletonColorScheme = remember(isLoading, currentColorScheme) {
        if (isLoading) {
            currentColorScheme.copy(
                surface = Color.Transparent,
                surfaceVariant = Color.Transparent,
                primaryContainer = Color.Transparent,
                secondaryContainer = Color.Transparent,
                tertiaryContainer = Color.Transparent,
                surfaceContainer = Color.Transparent,
                surfaceContainerHigh = Color.Transparent,
                surfaceContainerHighest = Color.Transparent,
                surfaceContainerLow = Color.Transparent,
                surfaceContainerLowest = Color.Transparent,
                // Ensure text/icons remain visible to the silhouette filter
                onSurface = highlightColor,
                onSurfaceVariant = highlightColor,
                onPrimaryContainer = highlightColor,
                outline = highlightColor
            )
        } else {
            currentColorScheme
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                contentSize = coordinates.size
            }
            .then(
                if (isLoading && contentSize != IntSize.Zero) {
                    Modifier
                        .graphicsLayer {
                            // Offscreen strategy allows blend modes to work correctly on the layer
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithContent {
                            val width = size.width
                            val height = size.height

                            val angleRad = (angleDegrees * PI / 180).toFloat()
                            val tanAngle = tan(angleRad)
                            val bandOffset = tanAngle * height
                            val totalTravel = width + bandOffset * 2
                            val currentX = (progress * totalTravel) - bandOffset

                            // Step 1: Create a silhouette paint
                            val silhouettePaint = Paint().apply {
                                colorFilter = ColorFilter.tint(baseColor)
                            }

                            // Step 2: Draw the content but ONLY use it as a mask (alpha mask)
                            // We save a layer to isolate the blending
                            drawContext.canvas.saveLayer(Rect(Offset.Zero, size), Paint())
                            
                            // Draw the silhouette (the grey shapes)
                            drawContext.canvas.saveLayer(Rect(Offset.Zero, size), silhouettePaint)
                            drawContent()
                            drawContext.canvas.restore()

                            // Step 3: Draw the glossy shimmer sweep using BlendMode.SrcIn
                            // This ensures the WHITE glint only appears ON TOP of the grey shapes
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
                                    end = Offset(currentX + bandOffset, height)
                                ),
                                blendMode = BlendMode.SrcIn
                            )

                            drawContext.canvas.restore()
                        }
                } else {
                    Modifier
                }
            )
    ) {
        // Apply the optimized ColorScheme for Skeleton generation
        MaterialTheme(colorScheme = skeletonColorScheme) {
            // Also override tonal elevation to 0 to prevent blocky shadows
            CompositionLocalProvider(
                androidx.compose.material3.LocalAbsoluteTonalElevation provides 0.dp
            ) {
                content()
            }
        }
    }
}

/**
 * Extension to convert Animatable float value to Compose State safely.
 */
@Composable
private fun <T, V : AnimationVector> androidx.compose.animation.core.Animatable<T, V>.asState(): State<T> {
    return remember(this) { derivedStateOf { value } }
}