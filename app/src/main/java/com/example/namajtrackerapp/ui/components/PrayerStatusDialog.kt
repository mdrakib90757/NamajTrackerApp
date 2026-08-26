package com.example.namajtrackerapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.namajtrackerapp.localization.AppStrings
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.ui.components.CustomTextField
import com.example.namajtrackerapp.ui.theme.ClayBrownPrimary
import com.example.namajtrackerapp.ui.theme.StatusLate
import com.example.namajtrackerapp.ui.theme.StatusLateBg
import com.example.namajtrackerapp.ui.theme.StatusMissed
import com.example.namajtrackerapp.ui.theme.StatusMissedBg
import com.example.namajtrackerapp.ui.theme.StatusNotYet
import com.example.namajtrackerapp.ui.theme.StatusNotYetBg
import com.example.namajtrackerapp.ui.theme.StatusOnTime
import com.example.namajtrackerapp.ui.theme.StatusOnTimeBg

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPrayerStatusBottomSheet(
    prayer: PrayerType,
    dateStr: String,
    currentStatus: PrayerStatus,
    currentNote: String?,
    language: AppLanguage,
    onStatusSelected: (PrayerStatus) -> Unit,
    onNoteSaved: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isEditingNote by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf(currentNote ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .imePadding()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.prayerName(prayer, language),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${AppStrings.prayerDescription(prayer, language)} • $dateStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Status Cards with micro-animations
            StatusOptionItem(
                title = AppStrings.statusName(PrayerStatus.ON_TIME, language),
                subtitle = if (language == AppLanguage.ENGLISH) "Prayed promptly within time window" else "সময়মতো সুন্দরভাবে আদায় করা হয়েছে",
                status = PrayerStatus.ON_TIME,
                isSelected = currentStatus == PrayerStatus.ON_TIME,
                badgeColor = StatusOnTime,
                badgeBg = StatusOnTimeBg,
                onClick = {
                    onStatusSelected(PrayerStatus.ON_TIME)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            StatusOptionItem(
                title = AppStrings.statusName(PrayerStatus.LATE, language),
                subtitle = if (language == AppLanguage.ENGLISH) "Prayed just before window expired" else "ওয়াক্তের শেষ দিকে কিছুটা দেরিতে আদায়",
                status = PrayerStatus.LATE,
                isSelected = currentStatus == PrayerStatus.LATE,
                badgeColor = StatusLate,
                badgeBg = StatusLateBg,
                onClick = {
                    onStatusSelected(PrayerStatus.LATE)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            StatusOptionItem(
                title = AppStrings.statusName(PrayerStatus.MISSED, language),
                subtitle = if (language == AppLanguage.ENGLISH) "Missed time window, to be made up (Qaza)" else "ওয়াক্ত পার হয়ে গেছে, কাযা আদায় করতে হবে",
                status = PrayerStatus.MISSED,
                isSelected = currentStatus == PrayerStatus.MISSED,
                badgeColor = StatusMissed,
                badgeBg = StatusMissedBg,
                onClick = {
                    onStatusSelected(PrayerStatus.MISSED)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            StatusOptionItem(
                title = AppStrings.statusName(PrayerStatus.NOT_YET, language),
                subtitle = if (language == AppLanguage.ENGLISH) "Upcoming prayer or pending" else "এখনো ওয়াক্ত আসেনি বা বাকি আছে",
                status = PrayerStatus.NOT_YET,
                isSelected = currentStatus == PrayerStatus.NOT_YET,
                badgeColor = StatusNotYet,
                badgeBg = StatusNotYetBg,
                onClick = {
                    onStatusSelected(PrayerStatus.NOT_YET)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Attached prayer note section
            if (!isEditingNote && currentNote.isNullOrBlank()) {
                TextButton(
                    onClick = { isEditingNote = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EditNote,
                        contentDescription = null,
                        tint = ClayBrownPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.addNoteForPrayer(language),
                        color = ClayBrownPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        CustomTextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            label = AppStrings.addNoteForPrayer(language),
                            placeholder = if (language == AppLanguage.BANGLA) "নোট লিখুন..." else "Write note...",
                            singleLine = false,
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                onNoteSaved(noteText.trim())
                                isEditingNote = false
                            }) {
                                Text(AppStrings.save(language), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
fun StatusOptionItem(
    title: String,
    subtitle: String,
    status: PrayerStatus,
    isSelected: Boolean,
    badgeColor: Color,
    badgeBg: Color,
    onClick: () -> Unit
) {
    val animatedBg by animateColorAsState(
        targetValue = if (isSelected) badgeBg else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        label = "statusBg"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) badgeColor else Color.Transparent,
        label = "statusBorder"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBg),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) animatedBorderColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(badgeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (status) {
                            PrayerStatus.ON_TIME -> Icons.Rounded.CheckCircle
                            PrayerStatus.LATE -> Icons.Rounded.Schedule
                            PrayerStatus.MISSED -> Icons.Rounded.Close
                            PrayerStatus.NOT_YET -> Icons.Rounded.Schedule
                        },
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(badgeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
