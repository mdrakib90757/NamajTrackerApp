package com.example.namajtrackerapp.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.R
import com.example.namajtrackerapp.ui.components.IslamicStarDivider
import com.example.namajtrackerapp.ui.theme.DarkBackground
import com.example.namajtrackerapp.ui.theme.GoldGlow
import com.example.namajtrackerapp.ui.theme.GoldLight
import com.example.namajtrackerapp.ui.theme.GoldPrimary
import com.example.namajtrackerapp.ui.theme.TextGold
import com.example.namajtrackerapp.ui.theme.TextGoldSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    autoNavigateDelayMs: Long = 2800L
) {
    val currentOnSplashFinished by rememberUpdatedState(onSplashFinished)
    // Animation states
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(20f) }
    val dividerAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val dotsAlpha = remember { Animatable(0f) }

    // Infinite breathing glow transition
    val infiniteTransition = rememberInfiniteTransition(label = "halo_glow")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )

    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )

    // Dot animations
    val dotPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dots"
    )

    LaunchedEffect(Unit) {
        // Staggered entrance animations
        launch {
            logoScale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(900, easing = FastOutSlowInEasing)
            )
        }
        delay(400)
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            )
        }
        delay(300)
        launch {
            dividerAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }
        delay(200)
        launch {
            taglineAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }
        delay(200)
        launch {
            dotsAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }

        // Auto transition after delay
        if (autoNavigateDelayMs > 0) {
            delay(autoNavigateDelayMs)
            currentOnSplashFinished()
        }
    }

    // Background gradient matching the reference:
    // Dark brown/chocolate gradient: center slightly lighter, top and bottom darker
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B0B04), // Top dark espresso
                        Color(0xFF281308), // Upper mid
                        Color(0xFF33180B), // Center glowing warm brown
                        Color(0xFF241006), // Lower mid
                        Color(0xFF140803)  // Bottom deepest espresso
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Tap anywhere to instantly continue
                currentOnSplashFinished()
            }
    ) {
        // Ambient golden particles & radial backlight
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.42f)
            val glowRadius = size.width * 0.55f * haloPulse

            // Soft radial backlight behind crescent
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x66B87B28),
                        Color(0x288C5212),
                        Color.Transparent
                    ),
                    center = center,
                    radius = glowRadius
                ),
                radius = glowRadius,
                center = center,
                alpha = haloAlpha
            )

            // Twinkling golden stars in the night sky
            val starPositions = listOf(
                Offset(size.width * 0.15f, size.height * 0.12f) to 2.0f,
                Offset(size.width * 0.82f, size.height * 0.15f) to 2.5f,
                Offset(size.width * 0.22f, size.height * 0.28f) to 1.8f,
                Offset(size.width * 0.88f, size.height * 0.32f) to 1.5f,
                Offset(size.width * 0.10f, size.height * 0.65f) to 2.2f,
                Offset(size.width * 0.90f, size.height * 0.68f) to 2.0f,
                Offset(size.width * 0.25f, size.height * 0.82f) to 1.6f,
                Offset(size.width * 0.78f, size.height * 0.86f) to 2.4f
            )

            for ((pos, r) in starPositions) {
                drawCircle(
                    color = GoldLight.copy(alpha = 0.55f * haloAlpha),
                    radius = r.dp.toPx(),
                    center = pos
                )
            }
        }

        // Main Splash Content Column (Exact Layout Matching Reference)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.9f))

            // Crescent Moon & Praying Figure Emblem
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                // Subtle warm glow halo behind the crescent
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(haloPulse)
                        .alpha(haloAlpha * 0.7f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x88FFCF70),
                                    Color(0x33DA9C38),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // High-res Crescent & Praying Figure Artwork
                Image(
                    painter = painterResource(id = R.drawable.crescent_emblem),
                    contentDescription = "Namaz Tracker Crescent Logo",
                    modifier = Modifier.size(220.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // App Title: "Namaz Tracker"
            Text(
                text = "Namaz Tracker",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = TextGold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset { IntOffset(0, textOffsetY.value.toInt()) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Islamic Star Divider with gradient lines
            IslamicStarDivider(
                modifier = Modifier
                    .alpha(dividerAlpha.value)
                    .padding(horizontal = 16.dp),
                color = GoldPrimary.copy(alpha = 0.85f),
                starSize = 15.dp,
                lineHeight = 1.2.dp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Tagline: "Track your prayers, strengthen your faith"
            Text(
                text = "Track your prayers, strengthen your faith",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.4.sp,
                color = TextGoldSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(taglineAlpha.value)
                    .padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1.1f))

            // Bottom 3 Dots Indicator (Animated)
            Row(
                modifier = Modifier
                    .alpha(dotsAlpha.value)
                    .padding(bottom = 44.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isCurrent = (dotPhase.toInt() % 3) == index
                    val dotAlpha = if (isCurrent) 1.0f else 0.4f
                    val dotSize = if (isCurrent) 7.dp else 5.5.dp
                    val dotColor = if (isCurrent) GoldPrimary else GoldLight.copy(alpha = 0.5f)

                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .background(
                                color = dotColor.copy(alpha = dotAlpha),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
