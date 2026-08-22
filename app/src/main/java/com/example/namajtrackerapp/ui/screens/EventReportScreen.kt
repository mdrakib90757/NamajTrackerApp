package com.example.namajtrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.ui.components.CustomDropdown
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.ui.components.NamajTopAppBar
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.LightBorder
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.example.namajtrackerapp.data.SampleDataProvider

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EventReportScreen(
    viewModel: NamazViewModel,
    onNavigateBack: () -> Unit
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val rawEvents by viewModel.events.collectAsState()
    val events = remember(rawEvents) {
        val sampleList = SampleDataProvider.getInitialEvents()
        (rawEvents + sampleList).distinctBy { it.titleEn }
    }
    val language = userSettings.language

    val calendarNow = Calendar.getInstance()
    val currentYear = calendarNow.get(Calendar.YEAR)
    val currentMonth = calendarNow.get(Calendar.MONTH) + 1 // 1-based index

    // Year options (current year down to 2024 + "ALL")
    val yearOptions = remember(currentYear) {
        listOf("ALL") + (currentYear downTo 2024).map { it.toString() }
    }

    // Month options (0 = ALL, 1..12 = Jan..Dec)
    val monthOptions = (0..12).toList()

    var selectedYearStr by remember { mutableStateOf(currentYear.toString()) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }

    fun getMonthName(monthIdx: Int): String {
        if (monthIdx == 0) return AppStrings.allMonths(language)
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, monthIdx - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val locale = if (language == AppLanguage.BANGLA) Locale("bn") else Locale.US
        return SimpleDateFormat("MMMM", locale).format(cal.time)
    }

    fun getYearName(yearStr: String): String {
        if (yearStr == "ALL") return AppStrings.allYears(language)
        return yearStr
    }

    // Filter events matching selected year and month
    val filteredEvents = remember(events, selectedYearStr, selectedMonth) {
        events.filter { event ->
            val dateParts = event.dateStr.split("-")
            if (dateParts.size == 3) {
                val eventYear = dateParts[0]
                val eventMonth = dateParts[1].toIntOrNull() ?: 0

                val yearMatches = selectedYearStr == "ALL" || eventYear == selectedYearStr || (event.isRecurringYearly)
                val monthMatches = selectedMonth == 0 || eventMonth == selectedMonth

                yearMatches && monthMatches
            } else {
                false
            }
        }
    }

    val totalEventsCount = filteredEvents.size
    val recurringCount = filteredEvents.count { it.isRecurringYearly }

    val categoryCounts = remember(filteredEvents) {
        EventCategory.entries.associateWith { cat ->
            filteredEvents.count { it.category == cat }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NamajTopAppBar(
            title = AppStrings.eventReportTitle(language),
            subtitle = AppStrings.eventReportSubtitle(language),
            onNavigateBack = onNavigateBack,
            actions = {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(SoftButterAccent, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IslamicStarCanvas(
                        modifier = Modifier.size(18.dp),
                        color = ClayBrownPrimary
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

            // Summary Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Events Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SoftButterAccent
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AppStrings.totalEventsCountLabel(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ClayBrownPrimary.copy(alpha = 0.85f)
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Event,
                                    contentDescription = null,
                                    tint = ClayBrownPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$totalEventsCount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ClayBrownPrimary
                                )
                            )
                        }
                    }

                    // Recurring Events Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = AppStrings.recurringEventsLabel(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.EventRepeat,
                                    contentDescription = null,
                                    tint = WarmGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$recurringCount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            // Category Breakdown Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = AppStrings.categoryBreakdownTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EventCategory.entries.forEach { category ->
                                val count = categoryCounts[category] ?: 0
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SoftButterAccent.copy(alpha = 0.5f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmGold.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = AppStrings.categoryName(category, language),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = ClayBrownPrimary
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(ClayBrownPrimary)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$count",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Events List Section Header
            item {
                Text(
                    text = AppStrings.eventsListTitle(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (filteredEvents.isEmpty()) {
                item {
                    Text(
                        text = AppStrings.noReportEventsMsg(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }
            } else {
                items(filteredEvents, key = { it.id }) { event ->
                    IslamicEventCard(
                        event = event,
                        language = language,
                        onEdit = { viewModel.openEditEvent(event) },
                        onDelete = { viewModel.requestDeleteEvent(event) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
