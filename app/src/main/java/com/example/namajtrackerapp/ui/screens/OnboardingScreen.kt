package com.example.namajtrackerapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.R
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusOnTime
import com.example.namajtrackerapp.ui.theme.StatusOnTimeBg
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    language: AppLanguage,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Islamic Star icon + Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(SoftButterAccent, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        IslamicStarCanvas(
                            modifier = Modifier.size(24.dp),
                            color = ClayBrownPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.appTitle(language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (pagerState.currentPage < 3) {
                    TextButton(onClick = onFinish) {
                        Text(
                            text = AppStrings.onboardingSkip(language),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // Pager for the 4 steps
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingStepCard(
                        imageRes = R.drawable.art_prayer_morning,
                        badge = if (language == AppLanguage.ENGLISH) "Daily Rhythm" else "প্রাত্যহিক নিয়ম",
                        title = AppStrings.onboardingStep1Title(language),
                        desc = AppStrings.onboardingStep1Desc(language)
                    )
                    1 -> OnboardingInteractiveStep(
                        language = language,
                        title = AppStrings.onboardingStep2Title(language),
                        desc = AppStrings.onboardingStep2Desc(language)
                    )
                    2 -> OnboardingStepCard(
                        imageRes = R.drawable.art_crescent_events,
                        badge = if (language == AppLanguage.ENGLISH) "Events & Journal" else "দিবস ও আমল",
                        title = AppStrings.onboardingStep3Title(language),
                        desc = AppStrings.onboardingStep3Desc(language)
                    )
                    3 -> OnboardingStepCard(
                        imageRes = R.drawable.art_privacy_shield,
                        badge = if (language == AppLanguage.ENGLISH) "100% Local & Private" else "১০০% নিরাপদ ও অফলাইন",
                        title = AppStrings.onboardingStep4Title(language),
                        desc = AppStrings.onboardingStep4Desc(language)
                    )
                }
            }

            // Bottom Navigation Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Progress Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(4) { index ->
                        val isSelected = pagerState.currentPage == index
                        val widthAnim by animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 8.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "dotWidth"
                        )
                        val colorAnim by animateColorAsState(
                            targetValue = if (isSelected) ClayBrownPrimary else MaterialTheme.colorScheme.outlineVariant,
                            label = "dotColor"
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(widthAnim)
                                .clip(CircleShape)
                                .background(colorAnim)
                                .clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                        )
                    }
                }

                // Action Button
                if (pagerState.currentPage < 3) {
                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ClayBrownPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = AppStrings.onboardingNext(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onFinish,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ClayBrownPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = AppStrings.onboardingGetStarted(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingStepCard(
    imageRes: Int,
    badge: String,
    title: String,
    desc: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image Card with soft shadow & warm border
        Card(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Badge
        Surface(
            color = SoftButterAccent,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ClayBrownPrimary
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = desc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun OnboardingInteractiveStep(
    language: AppLanguage,
    title: String,
    desc: String
) {
    var sampleStatus by remember { mutableStateOf(PrayerStatus.ON_TIME) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Interactive Live Demo Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    sampleStatus = when (sampleStatus) {
                        PrayerStatus.NOT_YET -> PrayerStatus.ON_TIME
                        PrayerStatus.ON_TIME -> PrayerStatus.LATE
                        PrayerStatus.LATE -> PrayerStatus.MISSED
                        PrayerStatus.MISSED -> PrayerStatus.ON_TIME
                    }
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(SoftButterAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Mosque,
                                contentDescription = null,
                                tint = ClayBrownPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Fajr" else "ফজর",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "05:15 AM • الفجر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Animated Status Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when (sampleStatus) {
                            PrayerStatus.ON_TIME -> StatusOnTimeBg
                            PrayerStatus.LATE -> Color(0xFFFEF3C7)
                            PrayerStatus.MISSED -> Color(0xFFFEE2E2)
                            PrayerStatus.NOT_YET -> Color(0xFFECE7E1)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = when (sampleStatus) {
                                    PrayerStatus.ON_TIME -> StatusOnTime
                                    PrayerStatus.LATE -> Color(0xFFD97706)
                                    PrayerStatus.MISSED -> Color(0xFFC53030)
                                    PrayerStatus.NOT_YET -> Color(0xFF8C827A)
                                },
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.statusName(sampleStatus, language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = when (sampleStatus) {
                                        PrayerStatus.ON_TIME -> StatusOnTime
                                        PrayerStatus.LATE -> Color(0xFFD97706)
                                        PrayerStatus.MISSED -> Color(0xFFC53030)
                                        PrayerStatus.NOT_YET -> Color(0xFF8C827A)
                                    }
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "👆 Tap this card to test toggling status" else "👆 স্ট্যাটাস পরিবর্তন করতে কার্ডটিতে ট্যাপ করুন",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = ClayBrownPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Badge
        Surface(
            color = SoftButterAccent,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Interactive Logging" else "সহজ ট্র্যাকিং",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ClayBrownPrimary
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = desc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
