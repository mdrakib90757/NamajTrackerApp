package com.example.namajtrackerapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.Nightlight
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.DailyPrayerRecord
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.ui.components.CircularPrayerProgress
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.ui.components.StreakBadge
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusLate
import com.example.namajtrackerapp.ui.theme.StatusLateBg
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.StatusMissedBg
import com.example.namajtrackerapp.ui.theme.StatusNotYet
import com.example.namajtrackerapp.ui.theme.StatusNotYetBg
import com.example.namajtrackerapp.ui.theme.StatusOnTime
import com.example.namajtrackerapp.ui.theme.StatusOnTimeBg
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: NamazViewModel
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val prayerRecords by viewModel.prayerRecords.collectAsState()
    val streakCount by viewModel.currentStreak.collectAsState()
    val todayCompletion by viewModel.todayCompletion.collectAsState()
    
    val language = userSettings.language
    val todayRecord = prayerRecords[viewModel.todayStr] ?: DailyPrayerRecord(viewModel.todayStr)

    var showSunnahSection by remember { mutableStateOf(false) }

    val gregorianDate = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US)
        sdf.format(Date())
    }

    val hijriDateStr = remember(language) {
        if (language == AppLanguage.ENGLISH) "14 Shaban 1446 AH" else "১৪ শাবান ১৪৪৬ হিজরি"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IslamicStarCanvas(
                            modifier = Modifier.size(20.dp),
                            color = WarmGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = hijriDateStr,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = WarmGold
                        )
                    }
                    Text(
                        text = gregorianDate,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                StreakBadge(streakDays = streakCount)
            }
        }

        // Today's Progress Card with Warm Butter highlight
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SoftButterAccent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.todayProgressTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ClayBrownPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.completedOfFive(todayCompletion.first, language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ClayBrownPrimary.copy(alpha = 0.85f)
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (todayCompletion.first == 5) {
                                if (language == AppLanguage.ENGLISH) "✨ All daily prayers fulfilled!" else "✨ আলহামদুলিল্লাহ, সকল ওয়াক্ত আদায় সম্পন্ন!"
                            } else {
                                AppStrings.streakSubtext(language)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = ClayBrownPrimary.copy(alpha = 0.75f)
                        )
                    }

                    CircularPrayerProgress(
                        completed = todayCompletion.first,
                        total = 5,
                        sizeDp = 76.dp,
                        progressColor = ClayBrownPrimary,
                        trackColor = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // Quick Action Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Mandatory Prayers" else "ফরজ নামাজ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                ElevatedButton(
                    onClick = { viewModel.markAllMandatoryDone(viewModel.todayStr) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = ClayBrownPrimary
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = WarmGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.markAllOnTime(language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // 5 Mandatory Prayer Cards
        items(
            listOf(
                PrayerType.FAJR,
                PrayerType.DHUHR,
                PrayerType.ASR,
                PrayerType.MAGHRIB,
                PrayerType.ISHA
            )
        ) { prayer ->
            val status = todayRecord.prayers[prayer] ?: PrayerStatus.NOT_YET
            val note = todayRecord.prayerNotes[prayer]

            PrayerCardItem(
                prayer = prayer,
                status = status,
                note = note,
                language = language,
                onStatusClick = {
                    viewModel.cyclePrayerStatus(prayer, viewModel.todayStr)
                },
                onCardClick = {
                    viewModel.openPrayerStatusPicker(prayer, viewModel.todayStr)
                }
            )
        }

        // Daily Ayah / Reflection Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FormatQuote,
                            contentDescription = null,
                            tint = WarmGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = AppStrings.dailyReflectionTitle(language),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = WarmGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.ENGLISH) {
                            "“Indeed, prayer has been decreed upon the believers a decree of specified times.”\n— Surah An-Nisa (4:103)"
                        } else {
                            "“নিশ্চয়ই নামাজ মুমিনদের ওপর নির্দিষ্ট সময়ে ফরজ করা হয়েছে।”\n— সূরা আন-নিসা (৪:১০৩)"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Sunnah & Nawafil Expandable Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { showSunnahSection = !showSunnahSection },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(WarmGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Nightlight,
                                contentDescription = null,
                                tint = WarmGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = AppStrings.sunnahSectionTitle(language),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Tahajjud, Duha, Witr" else "তাহাজ্জুদ, চাশত, বিতর",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = if (showSunnahSection) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (showSunnahSection) {
            items(
                listOf(
                    PrayerType.TAHAJJUD,
                    PrayerType.DUHA,
                    PrayerType.WITR
                )
            ) { prayer ->
                val status = todayRecord.prayers[prayer] ?: PrayerStatus.NOT_YET
                val note = todayRecord.prayerNotes[prayer]

                PrayerCardItem(
                    prayer = prayer,
                    status = status,
                    note = note,
                    language = language,
                    onStatusClick = {
                        viewModel.cyclePrayerStatus(prayer, viewModel.todayStr)
                    },
                    onCardClick = {
                        viewModel.openPrayerStatusPicker(prayer, viewModel.todayStr)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PrayerCardItem(
    prayer: PrayerType,
    status: PrayerStatus,
    note: String?,
    language: AppLanguage,
    onStatusClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val isCompleted = status.isCompleted

    val animatedBgColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "cardBg"
    )

    val prayerIcon: ImageVector = when (prayer) {
        PrayerType.FAJR -> Icons.Rounded.WbTwilight
        PrayerType.DHUHR -> Icons.Rounded.WbSunny
        PrayerType.ASR -> Icons.Rounded.WbSunny
        PrayerType.MAGHRIB -> Icons.Rounded.WbTwilight
        PrayerType.ISHA -> Icons.Rounded.Nightlight
        PrayerType.TAHAJJUD -> Icons.Rounded.Nightlight
        PrayerType.DUHA -> Icons.Rounded.WbSunny
        PrayerType.WITR -> Icons.Rounded.Mosque
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prayer Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) SoftButterAccent else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = prayerIcon,
                            contentDescription = null,
                            tint = if (isCompleted) ClayBrownPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.prayerName(prayer, language),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${prayer.arabicName})",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGold
                            )
                        }
                        Text(
                            text = "${prayer.defaultTimeEn} • ${AppStrings.prayerDescription(prayer, language).take(22)}...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                // Interactive Status Pill / Tick
                StatusInteractiveButton(
                    status = status,
                    language = language,
                    onClick = onStatusClick
                )
            }

            // Note preview if present
            if (!note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EditNote,
                            contentDescription = null,
                            tint = ClayBrownPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusInteractiveButton(
    status: PrayerStatus,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (status.isCompleted) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "statusScale"
    )

    val (bgColor, textColor, icon) = when (status) {
        PrayerStatus.ON_TIME -> Triple(StatusOnTimeBg, StatusOnTime, Icons.Rounded.CheckCircle)
        PrayerStatus.LATE -> Triple(StatusLateBg, StatusLate, Icons.Rounded.Schedule)
        PrayerStatus.MISSED -> Triple(StatusMissedBg, StatusMissed, Icons.Rounded.Close)
        PrayerStatus.NOT_YET -> Triple(StatusNotYetBg, StatusNotYet, Icons.Rounded.Schedule)
    }

    Surface(
        modifier = Modifier
            .scale(scaleAnim)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = bgColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = AppStrings.statusName(status, language),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}
