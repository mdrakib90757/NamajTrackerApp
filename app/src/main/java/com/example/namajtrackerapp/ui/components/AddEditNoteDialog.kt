package com.example.namajtrackerapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.model.SpiritualNote
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditNoteBottomSheet(
    noteToEdit: SpiritualNote?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        content: String,
        dateStr: String,
        linkedPrayer: PrayerType?,
        tags: List<String>
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.content ?: "") }
    var dateStr by remember { mutableStateOf(noteToEdit?.dateStr ?: today) }
    var selectedPrayer by remember { mutableStateOf(noteToEdit?.linkedPrayer) }

    val popularTags = listOf("Tadabbur", "Dua", "Quran Ayah", "Fajr", "Gratitude", "Ramadan", "Khushu")
    var selectedTags by remember { mutableStateOf(noteToEdit?.tags?.toSet() ?: setOf("Tadabbur")) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (noteToEdit != null && noteToEdit.id.isNotEmpty()) AppStrings.editNote(language) else AppStrings.addNote(language),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Note Title
            CustomTextField(
                value = title,
                onValueChange = { title = it },
                label = AppStrings.noteTitleLabel(language),
                placeholder = if (language == AppLanguage.ENGLISH) "e.g. Reflection on Surah Al-Kahf" else "যেমন: সূরা কাহাফ নিয়ে চিন্তা",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Note Content Editor
            CustomTextField(
                value = content,
                onValueChange = { content = it },
                label = AppStrings.noteContentLabel(language),
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 4,
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Optional Link to Prayer Dropdown
            val prayerOptions = remember(language) {
                listOf<PrayerType?>(null) + PrayerType.entries
            }
            CustomDropdown(
                label = AppStrings.linkToPrayerOptional(language),
                selectedOptionText = selectedPrayer?.let { AppStrings.prayerName(it, language) } ?: AppStrings.none(language),
                options = prayerOptions,
                onOptionSelected = { selectedPrayer = it },
                optionLabel = { prayer ->
                    if (prayer == null) AppStrings.none(language) else AppStrings.prayerName(prayer, language)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tags selection
            CustomLabel(text = AppStrings.tagsLabel(language))

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                popularTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTags = if (isSelected) {
                                selectedTags - tag
                            } else {
                                selectedTags + tag
                            }
                        },
                        label = { Text(tag) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftButterAccent,
                            selectedLabelColor = ClayBrownPrimary

                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(AppStrings.cancel(language))
                }

                Button(
                    onClick = {
                        val finalTitle = if (title.isNotBlank()) title.trim() else "Spiritual Reflection"
                        onSave(
                            finalTitle,
                            content.trim(),
                            dateStr,
                            selectedPrayer,
                            selectedTags.toList()
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ClayBrownPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(AppStrings.save(language), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
