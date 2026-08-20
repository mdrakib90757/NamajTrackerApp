package com.example.namajtrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.DailyPrayerRecord
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusLate
import com.example.namajtrackerapp.ui.theme.StatusLateBg
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.StatusMissedBg
import com.example.namajtrackerapp.ui.theme.StatusOnTime
import com.example.namajtrackerapp.ui.theme.StatusOnTimeBg
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CalendarScreen(
    viewModel: NamazViewModel,
    onOpenReport: () -> Unit = {}
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val prayerRecords by viewModel.prayerRecords.collectAsState()
    val selectedDateStr by viewModel.selectedDateStr.collectAsState()
    val monthlyStats by viewModel.monthlyStats.collectAsState()

    val language = userSettings.language

    // Month Navigation State
    val calendarState = remember { Calendar.getInstance() }
    var displayedYear by remember { mutableStateOf(calendarState.get(Calendar.YEAR)) }
    var displayedMonth by remember { mutableStateOf(calendarState.get(Calendar.MONTH)) } // 0-based

    val monthName = remember(displayedMonth, displayedYear, language) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val sdf = SimpleDateFormat("MMMM yyyy", if (language == AppLanguage.BANGLA) Locale("bn") else Locale.US)
        sdf.format(cal.time)
    }

    // Days in current viewed month
    val daysInMonth = remember(displayedYear, displayedMonth) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, etc.
        
        // Days list with empty leading pads
        val list = mutableListOf<String?>()
        val emptyLeading = (firstDayOfWeek - 1) % 7
        for (i in 0 until emptyLeading) {
            list.add(null)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        for (day in 1..totalDays) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            list.add(sdf.format(cal.time))
        }
        list
    }

    val selectedRecord = prayerRecords[selectedDateStr] ?: DailyPrayerRecord(selectedDateStr)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.calendarTitle(language),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Track consistency and past prayers" else "ধারাবাহিকতা ও বিগত দিনের নামাজের হিসাব",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SoftButterAccent, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                        tint = ClayBrownPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Stats Summary Cards (Monthly & Weekly Rates)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftButterAccent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.monthlyRate(language),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ClayBrownPrimary.copy(alpha = 0.8f)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${monthlyStats.completionPercentage}%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ClayBrownPrimary
                            )
                        )
                        Text(
                            text = "${monthlyStats.completedCount} ${if (language == AppLanguage.ENGLISH) "prayers fulfilled" else "ওয়াক্ত আদায়"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = ClayBrownPrimary.copy(alpha = 0.7f)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.onTimePercentage(language),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${monthlyStats.onTimePercentage}%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = StatusOnTime
                            )
                        )
                        Text(
                            text = "${monthlyStats.onTimeCount} ${if (language == AppLanguage.ENGLISH) "on time" else "যথাসময়ে"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Calendar Grid Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Month Navigation Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (displayedMonth == 0) {
                                    displayedMonth = 11
                                    displayedYear--
                                } else {
                                    displayedMonth--
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Previous Month"
                            )
                        }

                        Text(
                            text = monthName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = {
                                if (displayedMonth == 11) {
                                    displayedMonth = 0
                                    displayedYear++
                                } else {
                                    displayedMonth++
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Next Month"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Day of Week Headers
                    val dayHeaders = if (language == AppLanguage.ENGLISH) {
                        listOf("S", "M", "T", "W", "T", "F", "S")
                    } else {
                        listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        dayHeaders.forEach { header ->
                            Text(
                                text = header,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Grid
                    val rows = daysInMonth.chunked(7)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        rows.forEach { rowDays ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowDays.forEach { dateStr ->
                                    if (dateStr == null) {
                                        Spacer(modifier = Modifier.size(38.dp))
                                    } else {
                                        val dayNumber = dateStr.takeLast(2).toIntOrNull() ?: 1
                                        val record = prayerRecords[dateStr]
                                        val isSelected = dateStr == selectedDateStr
                                        val isToday = dateStr == viewModel.todayStr

                                        val completed = record?.mandatoryPrayersCompleted ?: 0
                                        val missed = record?.missedCount ?: 0

                                        val (statusColor, bgTint) = when {
                                            completed == 5 -> Pair(StatusOnTime, StatusOnTimeBg)
                                            completed in 1..4 -> Pair(StatusLate, StatusLateBg)
                                            missed > 0 -> Pair(StatusMissed, StatusMissedBg)
                                            else -> Pair(Color.Transparent, Color.Transparent)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isSelected -> ClayBrownPrimary
                                                        isToday -> SoftButterAccent
                                                        else -> bgTint
                                                    }
                                                )
                                                .border(
                                                    width = if (isSelected) 2.dp else if (isToday) 1.5.dp else 0.dp,
                                                    color = if (isSelected) WarmGold else if (isToday) ClayBrownPrimary else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    viewModel.setSelectedDate(dateStr)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "$dayNumber",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 13.sp
                                                    ),
                                                    color = when {
                                                        isSelected -> Color.White
                                                        isToday -> ClayBrownPrimary
                                                        else -> MaterialTheme.colorScheme.onSurface
                                                    }
                                                )
                                                // Status indicator dot
                                                if (statusColor != Color.Transparent && !isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .clip(CircleShape)
                                                            .background(statusColor)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(color = StatusOnTime, label = AppStrings.legendOnTime(language))
                        LegendItem(color = StatusLate, label = AppStrings.legendPartial(language))
                        LegendItem(color = StatusMissed, label = AppStrings.legendMissed(language))
                    }
                }
            }
        }


        // Selected Day Breakdown Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left column: Title + Subtitle stacked tightly
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = AppStrings.selectedDayTitle(selectedDateStr, language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = AppStrings.tapDayToEdit(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right column: Report button + optional reset, aligned to title's line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        onClick = onOpenReport,
                        shape = RoundedCornerShape(12.dp),
                        color = SoftButterAccent,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Analytics,
                                contentDescription = AppStrings.salatReportBtn(language),
                                tint = ClayBrownPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = AppStrings.salatReportBtn(language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = ClayBrownPrimary
                            )
                        }
                    }

                    if (selectedRecord.prayers.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.requestDeletePrayerRecord(selectedDateStr) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Reset Date Records",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 5 Prayers for the selected date
        items(
            listOf(
                PrayerType.FAJR,
                PrayerType.DHUHR,
                PrayerType.ASR,
                PrayerType.MAGHRIB,
                PrayerType.ISHA
            )
        ) { prayer ->
            val status = selectedRecord.prayers[prayer] ?: PrayerStatus.NOT_YET
            val note = selectedRecord.prayerNotes[prayer]

            PrayerCardItem(
                prayer = prayer,
                status = status,
                note = note,
                language = language,
                onStatusClick = {
                    viewModel.cyclePrayerStatus(prayer, selectedDateStr)
                },
                onCardClick = {
                    viewModel.openPrayerStatusPicker(prayer, selectedDateStr)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
