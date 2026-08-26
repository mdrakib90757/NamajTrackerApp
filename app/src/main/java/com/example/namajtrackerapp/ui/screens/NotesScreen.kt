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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.namajtrackerapp.model.SpiritualNote
import com.example.namajtrackerapp.ui.components.IslamicStarCanvas
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.viewmodel.NamazViewModel

import com.example.namajtrackerapp.ui.components.NamajTopAppBar

import androidx.compose.material.icons.rounded.Analytics
import com.example.namajtrackerapp.ui.theme.WarmGold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotesScreen(
    viewModel: NamazViewModel,
    onOpenReport: () -> Unit = {}
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.notesSearchQuery.collectAsState()
    val selectedTag by viewModel.selectedNoteTag.collectAsState()

    val language = userSettings.language

    val allTags = listOf("Tadabbur", "Dua", "Quran Ayah", "Fajr", "Khushu", "Ramadan", "Habit", "Goals")

    val filteredNotes = notes.filter { note ->
        val matchesSearch = searchQuery.isBlank() ||
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.content.contains(searchQuery, ignoreCase = true) ||
                note.tags.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesTag = selectedTag == null || note.tags.contains(selectedTag)

        matchesSearch && matchesTag
    }

    Scaffold(
        topBar = {
            NamajTopAppBar(
                title = AppStrings.notesTitle(language),
                subtitle = if (language == AppLanguage.ENGLISH) "Personal reflections, Duas, and Quranic thoughts" else "ব্যক্তিগত দোয়া, আয়াত ও আত্মশুদ্ধির ভাবনা",
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
                                contentDescription = AppStrings.noteReportBtn(language),
                                tint = ClayBrownPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = AppStrings.noteReportBtn(language),
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
                onClick = { viewModel.openAddNote() },
                containerColor = ClayBrownPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = AppStrings.addNote(language),
                    modifier = Modifier.size(28.dp)
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

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setNotesSearchQuery(it) },
                    placeholder = { Text(AppStrings.searchNotes(language)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setNotesSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ClayBrownPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    singleLine = true
                )
            }

            // Tag Filter Chips
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        selected = selectedTag == null,
                        onClick = { viewModel.setSelectedNoteTag(null) },
                        label = { Text(if (language == AppLanguage.ENGLISH) "All Notes" else "সকল নোট") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftButterAccent,
                            selectedLabelColor = ClayBrownPrimary
                        )
                    )
                    allTags.forEach { tag ->
                        FilterChip(
                            selected = selectedTag == tag,
                            onClick = {
                                viewModel.setSelectedNoteTag(if (selectedTag == tag) null else tag)
                            },
                            label = { Text(AppStrings.tagLabel(tag, language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftButterAccent,
                                selectedLabelColor = ClayBrownPrimary
                            )
                        )
                    }
                }
            }

            // Empty State
            if (filteredNotes.isEmpty()) {
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
                                painter = painterResource(id = R.drawable.art_journal_notes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = AppStrings.noNotesTitle(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppStrings.noNotesDesc(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                // Notes List
                items(filteredNotes, key = { it.id }) { note ->
                    SpiritualNoteCard(
                        note = note,
                        language = language,
                        onEdit = { viewModel.openEditNote(note) },
                        onDelete = { viewModel.requestDeleteNote(note) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Extra padding for FAB
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpiritualNoteCard(
    note: SpiritualNote,
    language: AppLanguage,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
            // Header Row: Date + Linked Prayer Badge + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = note.dateStr,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    note.linkedPrayer?.let { prayer ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoftButterAccent
                        ) {
                            Text(
                                text = "• ${AppStrings.prayerName(prayer, language)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ClayBrownPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
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

            Spacer(modifier = Modifier.height(8.dp))

            // Note Title
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Content
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            // Tags
            if (note.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    note.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "#${AppStrings.tagLabel(tag, language)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ClayBrownPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
