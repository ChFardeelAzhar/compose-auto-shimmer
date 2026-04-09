package com.example.composeautoshimmer.core

import androidx.compose.ui.graphics.Color

/**
 * Default configuration values for ComposeAutoShimmer library.
 * All shimmer components use these values unless explicitly overridden.
 */
object ShimmerDefaults {

    /** Base color of the shimmer skeleton background */
    val BaseColor = Color(0xFFD3D3D3)

    /** Highlight color — the bright sweep that creates the shimmer effect */
    val HighlightColor = Color(0xFFF8F7F7)

    /** Dark mode base color */
    val BaseColorDark = Color(0xFF1A1A1A)

    /** Dark mode highlight color */
    val HighlightColorDark = Color(0xFF333333)

    /** Duration of one shimmer sweep in milliseconds */
    const val DurationMillis = 1400

    /** Delay between each shimmer sweep cycle in milliseconds */
    const val DelayMillis = 500

    /** Angle of the shimmer sweep in degrees */
    const val AngleDegrees = 20f

    /** Width of the shimmer highlight band as fraction of total width */
    const val BandWidthFraction = 0.3f

    /** Corner radius applied to placeholder boxes in dp */
    const val CornerRadiusDp = 8f
}