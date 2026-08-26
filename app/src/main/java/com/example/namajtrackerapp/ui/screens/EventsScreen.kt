package com.example.namajtrackerapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.model.IslamicEvent
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.WarmGold
import com.example.namajtrackerapp.viewmodel.NamazViewModel
import java.util.Calendar
import com.example.namajtrackerapp.R

import androidx.compose.material.icons.rounded.Analytics

import com.example.namajtrackerapp.data.SampleDataProvider
import com.example.namajtrackerapp.ui.components.NamajTopAppBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventsScreen(
    viewModel: NamazViewModel,
    onOpenReport: () -> Unit = {}
) {
    val rawEvents by viewModel.events.collectAsState()
    val events = remember(rawEvents) {
        val sampleList = SampleDataProvider.getInitialEvents()
        (rawEvents + sampleList).distinctBy { it.titleEn }
    }
    val userSettings by viewModel.userSettings.collectAsState()
    val selectedCategory by viewModel.eventCategoryFilter.collectAsState()

    val language = userSettings.language
    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

    val filteredEvents = events.filter { event ->
        selectedCategory == null || event.category == selectedCategory
    }

    val thisYearCount = events.count { it.dateStr.startsWith(currentYear) || it.isRecurringYearly }
    val totalCount = events.size

    Scaffold(
        topBar = {
            NamajTopAppBar(
                title = AppStrings.eventsTitle(language),
                subtitle = if (language == AppLanguage.ENGLISH) "Blessed dates & personal fasting tracker" else "মহিমান্বিত রাত ও রোজা-আমলের দিনপঞ্জিকা",
                actions = {
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
                                contentDescription = AppStrings.eventReportBtn(language),
                                tint = ClayBrownPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = AppStrings.eventReportBtn(language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = ClayBrownPrimary
                            )
                        }
                    }

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddEvent() },
                containerColor = ClayBrownPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = AppStrings.addEvent(language),
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Summary Counter Pills (Lifetime + This Year)
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
                        border = BorderStroke(1.dp, ClayBrownPrimary.copy(alpha = 0.25f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = AppStrings.thisYear(language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ClayBrownPrimary.copy(alpha = 0.8f)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$thisYearCount",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ClayBrownPrimary
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Events in $currentYear" else "$currentYear সালের দিবস",
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
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = AppStrings.lifetime(language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalCount",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = AppStrings.totalEvents(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.setEventCategoryFilter(null) },
                        label = { Text(if (language == AppLanguage.ENGLISH) "All Events" else "সকল দিবস") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftButterAccent,
                            selectedLabelColor = ClayBrownPrimary
                        )
                    )
                    EventCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { viewModel.setEventCategoryFilter(cat) },
                            label = { Text(AppStrings.categoryName(cat, language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftButterAccent,
                                selectedLabelColor = ClayBrownPrimary
                            )
                        )
                    }
                }
            }

            // Empty State Illustration
            if (filteredEvents.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .size(160.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.art_crescent_events),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = AppStrings.noEventsTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppStrings.noEventsDesc(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                // Events List
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
                Spacer(modifier = Modifier.height(80.dp)) // Extra padding for FAB
            }
        }
    }
}

@Composable
fun IslamicEventCard(
    event: IslamicEvent,
    language: AppLanguage,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val displayTitle = if (language == AppLanguage.BANGLA && event.titleBn.isNotBlank()) {
        event.titleBn
    } else {
        event.titleEn
    }

    val displayHijri = if (language == AppLanguage.BANGLA && event.hijriDateBn.isNotBlank()) {
        event.hijriDateBn
    } else {
        event.hijriDateEn
    }

    val displayNote = if (language == AppLanguage.BANGLA && event.noteBn.isNotBlank()) {
        event.noteBn
    } else {
        event.noteEn
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Category & Recurring badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SoftButterAccent
                ) {
                    Text(
                        text = AppStrings.categoryName(event.category, language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ClayBrownPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (event.isRecurringYearly) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.EventRepeat,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = ClayBrownPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "Yearly" else "বার্ষিক",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onEdit,
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
                        onClick = onDelete,
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

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = displayTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Date & Hijri string
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarToday,
                        contentDescription = null,
                        tint = WarmGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.dateStr,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (displayHijri.isNotBlank()) {
                    Text(
                        text = "• $displayHijri",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGold
                    )
                }
            }

            // Note preview if present
            if (displayNote.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = displayNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(10.dp),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
