package com.rastilka.presentation.components_app.animate_vertical_alignment_as_state

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment

@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValueVertical: Float
): State<BiasAlignment> {
    val biasVertical by animateFloatAsState(targetBiasValueVertical, label = "")
    return remember { derivedStateOf { BiasAlignment(-1f, biasVertical) } }
}
