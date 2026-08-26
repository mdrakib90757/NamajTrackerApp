package com.example.namajtrackerapp.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Mosque
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.ui.components.CustomDropdown
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.ui.components.NamajTopAppBar
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.LightBorder
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReflectionReportScreen(
    viewModel: NamazViewModel,
    onNavigateBack: () -> Unit
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val notes by viewModel.notes.collectAsState()
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
        return if (language == AppLanguage.BANGLA) AppStrings.toBanglaDigits(yearStr) else yearStr
    }

    // Filter notes matching selected year and month
    val filteredNotes = remember(notes, selectedYearStr, selectedMonth) {
        notes.filter { note ->
            val dateParts = note.dateStr.split("-")
            if (dateParts.size >= 3) {
                val noteYear = dateParts[0]
                val noteMonth = dateParts[1].toIntOrNull() ?: 0

                val yearMatches = selectedYearStr == "ALL" || noteYear == selectedYearStr
                val monthMatches = selectedMonth == 0 || noteMonth == selectedMonth

                yearMatches && monthMatches
            } else {
                true
            }
        }
    }

    val totalNotesCount = filteredNotes.size
    val linkedToPrayerCount = filteredNotes.count { it.linkedPrayer != null }

    val tagCounts = remember(filteredNotes) {
        val map = mutableMapOf<String, Int>()
        filteredNotes.forEach { note ->
            note.tags.forEach { tag ->
                map[tag] = (map[tag] ?: 0) + 1
            }
        }
        map
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NamajTopAppBar(
            title = AppStrings.noteReportTitle(language),
            subtitle = AppStrings.noteReportSubtitle(language),
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

            // Filter Controls Card (Year & Month)
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
                                text = if (language == AppLanguage.ENGLISH) "Filter Period" else "সময়সীমা নির্বাচন",
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
                    // Total Reflections Card
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
                                    text = AppStrings.totalNotesCount(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ClayBrownPrimary.copy(alpha = 0.85f)
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.EditNote,
                                    contentDescription = null,
                                    tint = ClayBrownPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$totalNotesCount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ClayBrownPrimary
                                )
                            )
                        }
                    }

                    // Linked to Prayer Card
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
                                    text = AppStrings.linkedToPrayerCountLabel(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Mosque,
                                    contentDescription = null,
                                    tint = WarmGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$linkedToPrayerCount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            // Tag Breakdown Section
            if (tagCounts.isNotEmpty()) {
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
                                    imageVector = Icons.Rounded.Label,
                                    contentDescription = null,
                                    tint = ClayBrownPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = AppStrings.totalTagsCountLabel(language),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                tagCounts.forEach { (tag, count) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = SoftButterAccent,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmGold.copy(alpha = 0.4f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = AppStrings.tagLabel(tag, language),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = ClayBrownPrimary
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(CircleShape)
                                                    .background(ClayBrownPrimary),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$count",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section Header: Filtered Reflections List
            item {
                Text(
                    text = AppStrings.notesListTitle(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (filteredNotes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EditNote,
                                contentDescription = null,
                                tint = ClayBrownPrimary.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.noReportNotesMsg(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredNotes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    note.linkedPrayer?.let { prayer ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = SoftButterAccent
                                        ) {
                                            Text(
                                                text = AppStrings.prayerName(prayer, language),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = ClayBrownPrimary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.openEditNote(note) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Edit,
                                            contentDescription = "Edit",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.requestDeleteNote(note) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DeleteOutline,
                                            contentDescription = "Delete",
                                            tint = StatusMissed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = note.dateStr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ClayBrownPrimary
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    note.tags.take(3).forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        ) {
                                            Text(
                                                text = "#${AppStrings.tagLabel(tag, language)}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
