package com.example.namajtrackerapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.ui.components.CustomDropdown
import com.example.namajtrackerapp.ui.components.NamajTopAppBar
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.LightBorder
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusLate
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.StatusOnTime
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalatReportScreen(
    viewModel: NamazViewModel,
    onNavigateBack: () -> Unit
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val prayerRecords by viewModel.prayerRecords.collectAsState()
    val language = userSettings.language

    val calendarNow = Calendar.getInstance()
    val currentYear = calendarNow.get(Calendar.YEAR)
    val currentMonth = calendarNow.get(Calendar.MONTH) + 1 // 1-based index (1=Jan, 12=Dec)

    // Years available for selection (current year down to 2024 + "All Years")
    val yearOptions = remember(currentYear) {
        listOf("ALL") + (currentYear downTo 2024).map { it.toString() }
    }

    // Month options (0 = ALL, 1..12 = Jan..Dec)
    val monthOptions = (0..12).toList()

    var selectedYearStr by remember { mutableStateOf(currentYear.toString()) }
    var selectedMonth by remember { mutableStateOf(currentMonth) } // Defaults to current month

    // Helper to get localized Month display name
    fun getMonthName(monthIdx: Int): String {
        if (monthIdx == 0) return AppStrings.allMonths(language)
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, monthIdx - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val locale = if (language == AppLanguage.BANGLA) Locale("bn") else Locale.US
        return SimpleDateFormat("MMMM", locale).format(cal.time)
    }

    // Helper to get localized Year display name
    fun getYearName(yearStr: String): String {
        if (yearStr == "ALL") return AppStrings.allYears(language)
        return if (language == AppLanguage.BANGLA) AppStrings.toBanglaDigits(yearStr) else yearStr
    }

    // Filter records based on selected year and month
    val filteredRecords = remember(prayerRecords, selectedYearStr, selectedMonth) {
        prayerRecords.filter { (dateStr, _) ->
            val parts = dateStr.split("-")
            if (parts.size == 3) {
                val recordYear = parts[0]
                val recordMonth = parts[1].toIntOrNull() ?: 0

                val yearMatches = selectedYearStr == "ALL" || recordYear == selectedYearStr
                val monthMatches = selectedMonth == 0 || recordMonth == selectedMonth

                yearMatches && monthMatches
            } else {
                false
            }
        }.values.toList()
    }

    // Calculation functions
    fun getPrayerRakatCount(prayer: PrayerType): Int {
        return when (prayer) {
            PrayerType.FAJR -> 2
            PrayerType.DHUHR -> 4
            PrayerType.ASR -> 4
            PrayerType.MAGHRIB -> 3
            PrayerType.ISHA -> 4
            PrayerType.TAHAJJUD -> 2
            PrayerType.DUHA -> 2
            PrayerType.WITR -> 3
        }
    }

    // Calculate aggregated stats
    var totalCompletedPrayers = 0
    var totalOnTimeCount = 0
    var totalLateCount = 0
    var totalMissedCount = 0
    var totalRakatsCount = 0

    val prayerTypeStats = PrayerType.entries.associateWith { prayer ->
        var completed = 0
        var onTime = 0
        var late = 0
        var missed = 0
        var rakats = 0

        filteredRecords.forEach { record ->
            val status = record.prayers[prayer]
            when (status) {
                PrayerStatus.ON_TIME -> {
                    completed++
                    onTime++
                    rakats += getPrayerRakatCount(prayer)
                }
                PrayerStatus.LATE -> {
                    completed++
                    late++
                    rakats += getPrayerRakatCount(prayer)
                }
                PrayerStatus.MISSED -> {
                    missed++
                }
                null, PrayerStatus.NOT_YET -> {
                    // Unperformed
                }
            }
        }

        totalCompletedPrayers += completed
        totalOnTimeCount += onTime
        totalLateCount += late
        totalMissedCount += missed
        totalRakatsCount += rakats

        Triple(completed, onTime, Triple(late, missed, rakats))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NamajTopAppBar(
            title = AppStrings.salatReportTitle(language),
            subtitle = AppStrings.salatReportSubtitle(language),
            onNavigateBack = onNavigateBack,
            actions = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(SoftButterAccent, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Analytics,
                        contentDescription = null,
                        tint = ClayBrownPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Filter Controls (Year & Month Dropdowns)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.FilterList,
                                contentDescription = null,
                                tint = ClayBrownPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Filter Period" else "সময়সীমা নির্বাচন",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Year Filter Dropdown
                            CustomDropdown(
                                selectedOptionText = getYearName(selectedYearStr),
                                options = yearOptions,
                                onOptionSelected = { selectedYearStr = it },
                                optionLabel = { getYearName(it) },
                                label = AppStrings.filterYearLabel(language),
                                modifier = Modifier.weight(1f)
                            )

                            // Month Filter Dropdown
                            CustomDropdown(
                                selectedOptionText = getMonthName(selectedMonth),
                                options = monthOptions,
                                onOptionSelected = { selectedMonth = it },
                                optionLabel = { getMonthName(it) },
                                label = AppStrings.filterMonthLabel(language),
                                modifier = Modifier.weight(1.2f)
                            )
                        }
                    }
                }
            }

            // High Level Stats Summary Cards (Rakats & Total Wakto)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Rakat Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SoftButterAccent
                        ),
                        border = BorderStroke(1.dp, ClayBrownPrimary.copy(alpha = 0.25f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AppStrings.totalRakatCount(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ClayBrownPrimary.copy(alpha = 0.85f)
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Mosque,
                                    contentDescription = null,
                                    tint = ClayBrownPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$totalRakatsCount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ClayBrownPrimary
                                )
                            )
                            Text(
                                text = AppStrings.rakatUnit(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = ClayBrownPrimary.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Total Wakto Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AppStrings.totalWaktoCount(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusOnTime,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$totalCompletedPrayers",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = AppStrings.waktoUnit(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Detailed Status Ratio Card (On-Time, Late, Qaza)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.statsBreakdown(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatusCounterPill(
                                count = totalOnTimeCount,
                                label = AppStrings.onTimeCountLabel(language),
                                color = StatusOnTime
                            )
                            StatusCounterPill(
                                count = totalLateCount,
                                label = AppStrings.lateCountLabel(language),
                                color = StatusLate
                            )
                            StatusCounterPill(
                                count = totalMissedCount,
                                label = AppStrings.qazaCountLabel(language),
                                color = StatusMissed
                            )
                        }
                    }
                }
            }

            // Section Header: Wakto Performance Breakdown
            item {
                Text(
                    text = AppStrings.prayerBreakdownTitle(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // List of Prayer Wakto Cards
            items(PrayerType.entries.toList()) { prayer ->
                val stats = prayerTypeStats[prayer]
                val completed = stats?.first ?: 0
                val onTime = stats?.second ?: 0
                val late = stats?.third?.first ?: 0
                val missed = stats?.third?.second ?: 0
                val rakats = stats?.third?.third ?: 0

                PrayerReportCard(
                    prayer = prayer,
                    completed = completed,
                    onTime = onTime,
                    late = late,
                    missed = missed,
                    rakats = rakats,
                    language = language
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatusCounterPill(
    count: Int,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

@Composable
fun PrayerReportCard(
    prayer: PrayerType,
    completed: Int,
    onTime: Int,
    late: Int,
    missed: Int,
    rakats: Int,
    language: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = AppStrings.prayerName(prayer, language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ClayBrownPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$rakats ${AppStrings.rakatUnit(language)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = ClayBrownPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = AppStrings.prayerDescription(prayer, language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$completed ${AppStrings.waktoUnit(language)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = StatusOnTime
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "(${AppStrings.onTimeCountLabel(language)}: $onTime | ${AppStrings.lateCountLabel(language)}: $late)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
