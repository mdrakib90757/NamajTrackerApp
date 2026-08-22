package com.example.namajtrackerapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Nightlight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.SoftButterAccent
import com.example.namajtrackerapp.ui.theme.WarmGold

data class HijriMonthItem(
    val id: Int,
    val nameEn: String,
    val nameBn: String
)

val HIJRI_MONTHS = listOf(
    HijriMonthItem(1, "Muharram", "মুহররম"),
    HijriMonthItem(2, "Safar", "সফর"),
    HijriMonthItem(3, "Rabi' al-Awwal", "রবিউল আউয়াল"),
    HijriMonthItem(4, "Rabi' al-Thani", "রবিউস সানি"),
    HijriMonthItem(5, "Jumada al-Awwal", "জুমাদাল আউয়াল"),
    HijriMonthItem(6, "Jumada al-Thani", "জুমাদাস সানি"),
    HijriMonthItem(7, "Rajab", "রজব"),
    HijriMonthItem(8, "Sha'ban", "শাবান"),
    HijriMonthItem(9, "Ramadan", "রমজান"),
    HijriMonthItem(10, "Shawwal", "শাওয়াল"),
    HijriMonthItem(11, "Dhu al-Qi'dah", "জিলকদ"),
    HijriMonthItem(12, "Dhu al-Hijjah", "জিলহজ")
)

fun toBanglaNum(number: Int): String {
    val enDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    val str = number.toString()
    val sb = StringBuilder()
    for (ch in str) {
        val idx = enDigits.indexOf(ch)
        if (idx != -1) {
            sb.append(bnDigits[idx])
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HijriDatePickerDialog(
    initialHijriDate: String,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onDateSelected: (hijriDateEn: String, hijriDateBn: String) -> Unit
) {
    // Parse initial date if possible
    var selectedDay by remember { mutableIntStateOf(14) }
    var selectedMonthIndex by remember { mutableIntStateOf(7) } // Sha'ban default (index 7)
    var selectedYear by remember { mutableIntStateOf(1446) }

    remember(initialHijriDate) {
        try {
            if (initialHijriDate.isNotBlank()) {
                val parts = initialHijriDate.split(" ")
                if (parts.isNotEmpty()) {
                    parts[0].toIntOrNull()?.let { selectedDay = it.coerceIn(1, 30) }
                }
                HIJRI_MONTHS.forEachIndexed { index, month ->
                    if (initialHijriDate.contains(month.nameEn, ignoreCase = true) ||
                        initialHijriDate.contains(month.nameBn, ignoreCase = true)
                    ) {
                        selectedMonthIndex = index
                    }
                }
                parts.find { it.endsWith("AH") || it.toIntOrNull() != null }?.replace("AH", "")?.trim()?.toIntOrNull()?.let {
                    if (it in 1400..1500) selectedYear = it
                }
            }
        } catch (_: Exception) {}
    }

    val selectedMonth = HIJRI_MONTHS[selectedMonthIndex]

    // Formatted output strings
    val dayBnStr = remember(selectedDay) { toBanglaNum(selectedDay) }
    val yearBnStr = remember(selectedYear) { toBanglaNum(selectedYear) }

    val formattedHijriEn = "$selectedDay ${selectedMonth.nameEn} $selectedYear AH"
    val formattedHijriBn = "$dayBnStr ${selectedMonth.nameBn} $yearBnStr হিজরি"

    val yearsList = (1440..1460).toList()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Title with Moon icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(WarmGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Nightlight,
                            contentDescription = null,
                            tint = WarmGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.BANGLA) "হিজরি তারিখ নির্বাচন করুন" else "Select Hijri Date",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Preview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) formattedHijriBn else formattedHijriEn,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = if (language == AppLanguage.BANGLA) formattedHijriEn else formattedHijriBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Month & Year CustomDropdowns Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Month Dropdown
                    CustomDropdown(
                        selectedOptionText = if (language == AppLanguage.BANGLA) "${toBanglaNum(selectedMonth.id)}. ${selectedMonth.nameBn}" else "${selectedMonth.id}. ${selectedMonth.nameEn}",
                        options = HIJRI_MONTHS,
                        onOptionSelected = { month ->
                            selectedMonthIndex = HIJRI_MONTHS.indexOf(month)
                        },
                        optionLabel = { month ->
                            if (language == AppLanguage.BANGLA) "${toBanglaNum(month.id)}. ${month.nameBn}" else "${month.id}. ${month.nameEn}"
                        },
                        label = if (language == AppLanguage.BANGLA) "মাস" else "Month",
                        maxHeight = 220.dp,
                        modifier = Modifier.weight(1.3f)
                    )

                    // Year Dropdown
                    CustomDropdown(
                        selectedOptionText = if (language == AppLanguage.BANGLA) "$yearBnStr হিজরি" else "$selectedYear AH",
                        options = yearsList,
                        onOptionSelected = { year ->
                            selectedYear = year
                        },
                        optionLabel = { year ->
                            if (language == AppLanguage.BANGLA) "${toBanglaNum(year)} হিজরি" else "$year AH"
                        },
                        label = if (language == AppLanguage.BANGLA) "বছর" else "Year",
                        maxHeight = 220.dp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Day Selector Grid Label
                Text(
                    text = if (language == AppLanguage.BANGLA) "দিন নির্বাচন করুন (১-৩০):" else "Select Day (1-30):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 30 Days Grid Picker
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items((1..30).toList()) { day ->
                        val isSelected = day == selectedDay
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .clickable { selectedDay = day },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (language == AppLanguage.BANGLA) toBanglaNum(day) else day.toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
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
                            onDateSelected(formattedHijriEn, formattedHijriBn)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ClayBrownPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.BANGLA) "ঠিক আছে" else "OK",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
