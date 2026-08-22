package com.example.namajtrackerapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Nightlight
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.model.IslamicEvent
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.WarmGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventBottomSheet(
    eventToEdit: IslamicEvent?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (
        titleEn: String,
        titleBn: String,
        dateStr: String,
        hijriDateEn: String,
        hijriDateBn: String,
        noteEn: String,
        noteBn: String,
        isRecurring: Boolean,
        category: EventCategory
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    var titleEn by remember { mutableStateOf(eventToEdit?.titleEn ?: "") }
    var titleBn by remember { mutableStateOf(eventToEdit?.titleBn ?: "") }
    var dateStr by remember { mutableStateOf(eventToEdit?.dateStr ?: today) }
    var hijriDateEn by remember { mutableStateOf(eventToEdit?.hijriDateEn ?: "") }
    var hijriDateBn by remember { mutableStateOf(eventToEdit?.hijriDateBn ?: "") }
    var showHijriDatePicker by remember { mutableStateOf(false) }
    var noteEn by remember { mutableStateOf(eventToEdit?.noteEn ?: "") }
    var isRecurring by remember { mutableStateOf(eventToEdit?.isRecurringYearly ?: true) }
    var selectedCategory by remember { mutableStateOf(eventToEdit?.category ?: EventCategory.SPECIAL_NIGHT) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
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
                    text = if (eventToEdit != null) AppStrings.editEvent(language) else AppStrings.addEvent(language),
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

            // Title English
            CustomTextField(
                value = titleEn,
                onValueChange = { titleEn = it },
                label = "${AppStrings.eventTitleLabel(language)} (English)",
                placeholder = if (language == AppLanguage.ENGLISH) "e.g. Laylatul Qadr" else "উদাহরণ: Laylatul Qadr",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title Bangla
            CustomTextField(
                value = titleBn,
                onValueChange = { titleBn = it },
                label = "${AppStrings.eventTitleLabel(language)} (বাংলা)",
                placeholder = if (titleEn.isNotBlank()) titleEn else (if (language == AppLanguage.ENGLISH) "e.g. Shab-e-Qadr" else "উদাহরণ: লাইলাতুল কদর"),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date Input (Native DatePicker on tap, non-editable by typing, styled with App ClayBrown theme)
            val context = LocalContext.current
            val openDatePicker = {
                val cal = Calendar.getInstance()
                try {
                    val parts = dateStr.split("-")
                    if (parts.size == 3) {
                        cal.set(Calendar.YEAR, parts[0].toInt())
                        cal.set(Calendar.MONTH, parts[1].toInt() - 1)
                        cal.set(Calendar.DAY_OF_MONTH, parts[2].toInt())
                    }
                } catch (_: Exception) {}

                android.app.DatePickerDialog(
                    context,
                    com.example.namajtrackerapp.R.style.CustomDatePickerTheme,
                    { _, yr, mnth, dy ->
                        val formattedMonth = String.format(Locale.US, "%02d", mnth + 1)
                        val formattedDay = String.format(Locale.US, "%02d", dy)
                        dateStr = "$yr-$formattedMonth-$formattedDay"
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomTextField(
                    value = dateStr,
                    onValueChange = {},
                    label = AppStrings.eventDateLabel(language),
                    placeholder = "YYYY-MM-DD",
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Event,
                            contentDescription = "Select Date",
                            tint = ClayBrownPrimary
                        )
                    },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { openDatePicker() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Dropdown
            CustomDropdown(
                label = AppStrings.categoryLabel(language),
                selectedOptionText = AppStrings.categoryName(selectedCategory, language),
                options = EventCategory.entries,
                onOptionSelected = { selectedCategory = it },
                optionLabel = { cat -> AppStrings.categoryName(cat, language) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hijri Date (Read-only, opens HijriDatePickerDialog on tap)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomTextField(
                    value = if (language == AppLanguage.BANGLA && hijriDateBn.isNotBlank()) hijriDateBn else hijriDateEn,
                    onValueChange = {},
                    label = if (language == AppLanguage.ENGLISH) "Hijri Date" else "হিজরি তারিখ",
                    placeholder = if (language == AppLanguage.ENGLISH) "Tap to select Hijri Date" else "হিজরি তারিখ নির্বাচন করতে ট্যাপ করুন",
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Nightlight,
                            contentDescription = "Select Hijri Date",
                            tint = WarmGold
                        )
                    },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { showHijriDatePicker = true }
                )
            }

            if (showHijriDatePicker) {
                HijriDatePickerDialog(
                    initialHijriDate = if (language == AppLanguage.BANGLA) hijriDateBn else hijriDateEn,
                    language = language,
                    onDismiss = { showHijriDatePicker = false },
                    onDateSelected = { hEn, hBn ->
                        hijriDateEn = hEn
                        hijriDateBn = hBn
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Personal Note / Intention
            CustomTextField(
                value = noteEn,
                onValueChange = { noteEn = it },
                label = AppStrings.eventNoteLabel(language),
                placeholder = if (language == AppLanguage.ENGLISH) "Enter personal notes or special intentions..." else "ব্যক্তিগত নোট বা বিশেষ আমলের বিবরণ...",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Recurring Yearly Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isRecurring = !isRecurring }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.recurringYearly(language),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Repeats annually in the Islamic or solar calendar" else "প্রতি বছর এই দিনে স্বয়ংক্রিয়ভাবে স্মরণ করিয়ে দেওয়া হবে",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ClayBrownPrimary,
                        checkedTrackColor = SoftButterAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = AppStrings.cancel(language),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        val finalTitleEn = if (titleEn.isNotBlank()) titleEn.trim() else "Islamic Event"
                        val finalTitleBn = if (titleBn.isNotBlank()) titleBn.trim() else finalTitleEn
                        onSave(
                            finalTitleEn,
                            finalTitleBn,
                            dateStr.trim(),
                            hijriDateEn.trim(),
                            if (hijriDateBn.isNotBlank()) hijriDateBn.trim() else hijriDateEn.trim(),
                            noteEn.trim(),
                            noteEn.trim(),
                            isRecurring,
                            selectedCategory
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
