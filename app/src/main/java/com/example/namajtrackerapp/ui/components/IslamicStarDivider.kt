package com.example.namajtrackerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.namajtrackerapp.ui.theme.GoldPrimary

@Composable
fun IslamicStarDivider(
    modifier: Modifier = Modifier,
    color: Color = GoldPrimary.copy(alpha = 0.85f),
    starSize: Dp = 15.dp,
    lineHeight: Dp = 1.2.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left gradient line fading from transparent to gold color
        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineHeight)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            color
                        )
                    )
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Center 8-pointed Islamic Star canvas
        IslamicStarCanvas(
            modifier = Modifier.size(starSize),
            color = color,
            points = 8
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Right gradient line fading from gold color to transparent
        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineHeight)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color,
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
