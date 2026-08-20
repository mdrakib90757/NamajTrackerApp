package com.example.namajtrackerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.namajtrackerapp.data.NamazRepository
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.AppThemeMode
import com.example.namajtrackerapp.model.DailyPrayerRecord
import com.example.namajtrackerapp.model.EventCategory
import com.example.namajtrackerapp.model.IslamicEvent
import com.example.namajtrackerapp.model.PrayerStatus
import com.example.namajtrackerapp.model.PrayerType
import com.example.namajtrackerapp.model.SpiritualNote
import com.example.namajtrackerapp.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class DeleteItemTarget {
    data class NoteTarget(val note: SpiritualNote) : DeleteItemTarget()
    data class EventTarget(val event: IslamicEvent) : DeleteItemTarget()
    data class PrayerRecordTarget(val dateStr: String) : DeleteItemTarget()
}

data class DeleteDialogState(
    val isOpen: Boolean = false,
    val target: DeleteItemTarget? = null
)

data class MonthlyStats(
    val totalMandatoryPrayers: Int = 0,
    val completedCount: Int = 0,
    val onTimeCount: Int = 0,
    val lateCount: Int = 0,
    val missedCount: Int = 0,
    val completionPercentage: Int = 0,
    val onTimePercentage: Int = 0
)

class NamazViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NamazRepository(application)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val todayStr: String = dateFormat.format(Date())

    val userSettings: StateFlow<UserSettings> = repository.userSettingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserSettings())

    val prayerRecords: StateFlow<Map<String, DailyPrayerRecord>> = repository.prayerRecordsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val events: StateFlow<List<IslamicEvent>> = repository.eventsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val notes: StateFlow<List<SpiritualNote>> = repository.notesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Selected date for Calendar and History inspection
    private val _selectedDateStr = MutableStateFlow(todayStr)
    val selectedDateStr: StateFlow<String> = _selectedDateStr.asStateFlow()

    // Active Prayer status picker dialog / modal
    private val _activePrayerPicker = MutableStateFlow<Pair<PrayerType, String>?>(null)
    val activePrayerPicker: StateFlow<Pair<PrayerType, String>?> = _activePrayerPicker.asStateFlow()

    // Delete Confirmation Dialog State
    private val _deleteDialogState = MutableStateFlow(DeleteDialogState())
    val deleteDialogState: StateFlow<DeleteDialogState> = _deleteDialogState.asStateFlow()

    // Add / Edit Event Sheet State
    private val _editingEvent = MutableStateFlow<IslamicEvent?>(null)
    val editingEvent: StateFlow<IslamicEvent?> = _editingEvent.asStateFlow()
    private val _isEventEditorOpen = MutableStateFlow(false)
    val isEventEditorOpen: StateFlow<Boolean> = _isEventEditorOpen.asStateFlow()

    // Add / Edit Note Sheet State
    private val _editingNote = MutableStateFlow<SpiritualNote?>(null)
    val editingNote: StateFlow<SpiritualNote?> = _editingNote.asStateFlow()
    private val _isNoteEditorOpen = MutableStateFlow(false)
    val isNoteEditorOpen: StateFlow<Boolean> = _isNoteEditorOpen.asStateFlow()

    // Search and filters
    private val _notesSearchQuery = MutableStateFlow("")
    val notesSearchQuery: StateFlow<String> = _notesSearchQuery.asStateFlow()

    private val _selectedNoteTag = MutableStateFlow<String?>(null)
    val selectedNoteTag: StateFlow<String?> = _selectedNoteTag.asStateFlow()

    private val _eventCategoryFilter = MutableStateFlow<EventCategory?>(null)
    val eventCategoryFilter: StateFlow<EventCategory?> = _eventCategoryFilter.asStateFlow()

    // Calculated Streak
    val currentStreak: StateFlow<Int> = prayerRecords.mapToStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 7)

    // Today's completion
    val todayCompletion: StateFlow<Pair<Int, Int>> = combine(prayerRecords, _selectedDateStr) { records, _ ->
        val record = records[todayStr] ?: DailyPrayerRecord(todayStr)
        val completed = record.mandatoryPrayersCompleted
        Pair(completed, 5)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(4, 5))

    // Monthly stats for selected month
    val monthlyStats: StateFlow<MonthlyStats> = prayerRecords.combine(_selectedDateStr) { records, selDate ->
        val monthPrefix = if (selDate.length >= 7) selDate.substring(0, 7) else todayStr.substring(0, 7)
        val monthRecords = records.filter { it.key.startsWith(monthPrefix) }.values
        
        var totalMandatory = 0
        var completed = 0
        var onTime = 0
        var late = 0
        var missed = 0

        for (record in monthRecords) {
            for (p in PrayerType.entries.filter { it.isMandatory }) {
                totalMandatory++
                val status = record.prayers[p] ?: PrayerStatus.NOT_YET
                if (status == PrayerStatus.ON_TIME) {
                    completed++
                    onTime++
                } else if (status == PrayerStatus.LATE) {
                    completed++
                    late++
                } else if (status == PrayerStatus.MISSED) {
                    missed++
                }
            }
        }

        if (totalMandatory == 0) totalMandatory = 1
        val completionPct = (completed * 100) / totalMandatory
        val onTimePct = if (completed > 0) (onTime * 100) / completed else 0

        MonthlyStats(
            totalMandatoryPrayers = totalMandatory,
            completedCount = completed,
            onTimeCount = onTime,
            lateCount = late,
            missedCount = missed,
            completionPercentage = completionPct,
            onTimePercentage = onTimePct
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MonthlyStats())

    // --- Actions ---

    fun setSelectedDate(dateStr: String) {
        _selectedDateStr.value = dateStr
    }

    fun openPrayerStatusPicker(prayer: PrayerType, dateStr: String = todayStr) {
        _activePrayerPicker.value = Pair(prayer, dateStr)
    }

    fun closePrayerStatusPicker() {
        _activePrayerPicker.value = null
    }

    fun setPrayerStatus(prayer: PrayerType, status: PrayerStatus, dateStr: String = todayStr) {
        val currentMap = prayerRecords.value.toMutableMap()
        val record = currentMap[dateStr] ?: DailyPrayerRecord(dateStr)
        val updatedPrayers = record.prayers.toMutableMap()
        updatedPrayers[prayer] = status
        
        currentMap[dateStr] = record.copy(prayers = updatedPrayers)
        viewModelScope.launch {
            repository.savePrayerRecords(currentMap)
        }
        closePrayerStatusPicker()
    }

    fun cyclePrayerStatus(prayer: PrayerType, dateStr: String = todayStr) {
        val record = prayerRecords.value[dateStr] ?: DailyPrayerRecord(dateStr)
        val currentStatus = record.prayers[prayer] ?: PrayerStatus.NOT_YET
        val nextStatus = when (currentStatus) {
            PrayerStatus.NOT_YET -> PrayerStatus.ON_TIME
            PrayerStatus.ON_TIME -> PrayerStatus.LATE
            PrayerStatus.LATE -> PrayerStatus.MISSED
            PrayerStatus.MISSED -> PrayerStatus.NOT_YET
        }
        setPrayerStatus(prayer, nextStatus, dateStr)
    }

    fun markAllMandatoryDone(dateStr: String = todayStr) {
        val currentMap = prayerRecords.value.toMutableMap()
        val record = currentMap[dateStr] ?: DailyPrayerRecord(dateStr)
        val updatedPrayers = record.prayers.toMutableMap()
        for (p in PrayerType.entries.filter { it.isMandatory }) {
            if (updatedPrayers[p] == null || updatedPrayers[p] == PrayerStatus.NOT_YET) {
                updatedPrayers[p] = PrayerStatus.ON_TIME
            }
        }
        currentMap[dateStr] = record.copy(prayers = updatedPrayers)
        viewModelScope.launch {
            repository.savePrayerRecords(currentMap)
        }
    }

    fun setPrayerNote(prayer: PrayerType, noteText: String, dateStr: String = todayStr) {
        val currentMap = prayerRecords.value.toMutableMap()
        val record = currentMap[dateStr] ?: DailyPrayerRecord(dateStr)
        val updatedNotes = record.prayerNotes.toMutableMap()
        if (noteText.isBlank()) {
            updatedNotes.remove(prayer)
        } else {
            updatedNotes[prayer] = noteText
        }
        currentMap[dateStr] = record.copy(prayerNotes = updatedNotes)
        viewModelScope.launch {
            repository.savePrayerRecords(currentMap)
        }
    }

    // --- Events Management ---

    fun openAddEvent() {
        _editingEvent.value = null
        _isEventEditorOpen.value = true
    }

    fun openEditEvent(event: IslamicEvent) {
        _editingEvent.value = event
        _isEventEditorOpen.value = true
    }

    fun closeEventEditor() {
        _isEventEditorOpen.value = false
        _editingEvent.value = null
    }

    fun saveEvent(
        titleEn: String,
        titleBn: String,
        dateStr: String,
        hijriDateEn: String,
        hijriDateBn: String,
        noteEn: String,
        noteBn: String,
        isRecurring: Boolean,
        category: EventCategory
    ) {
        val currentList = events.value.toMutableList()
        val existing = _editingEvent.value

        if (existing != null) {
            val updated = existing.copy(
                titleEn = titleEn,
                titleBn = if (titleBn.isBlank()) titleEn else titleBn,
                dateStr = dateStr,
                hijriDateEn = hijriDateEn,
                hijriDateBn = hijriDateBn,
                noteEn = noteEn,
                noteBn = noteBn,
                isRecurringYearly = isRecurring,
                category = category
            )
            val index = currentList.indexOfFirst { it.id == existing.id }
            if (index >= 0) {
                currentList[index] = updated
            }
        } else {
            val newEvent = IslamicEvent(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                titleEn = titleEn,
                titleBn = if (titleBn.isBlank()) titleEn else titleBn,
                dateStr = dateStr,
                hijriDateEn = hijriDateEn,
                hijriDateBn = hijriDateBn,
                noteEn = noteEn,
                noteBn = noteBn,
                isRecurringYearly = isRecurring,
                category = category
            )
            currentList.add(0, newEvent)
        }

        viewModelScope.launch {
            repository.saveEvents(currentList)
        }
        closeEventEditor()
    }

    fun setEventCategoryFilter(cat: EventCategory?) {
        _eventCategoryFilter.value = cat
    }

    // --- Notes Management ---

    fun openAddNote(linkedPrayer: PrayerType? = null) {
        _editingNote.value = SpiritualNote(
            id = "",
            title = "",
            content = "",
            dateStr = todayStr,
            linkedPrayer = linkedPrayer
        )
        _isNoteEditorOpen.value = true
    }

    fun openEditNote(note: SpiritualNote) {
        _editingNote.value = note
        _isNoteEditorOpen.value = true
    }

    fun closeNoteEditor() {
        _isNoteEditorOpen.value = false
        _editingNote.value = null
    }

    fun saveNote(
        title: String,
        content: String,
        dateStr: String,
        linkedPrayer: PrayerType?,
        tags: List<String>
    ) {
        val currentList = notes.value.toMutableList()
        val existing = _editingNote.value

        if (existing != null && existing.id.isNotEmpty()) {
            val updated = existing.copy(
                title = title,
                content = content,
                dateStr = dateStr,
                linkedPrayer = linkedPrayer,
                tags = tags,
                updatedAt = System.currentTimeMillis()
            )
            val index = currentList.indexOfFirst { it.id == existing.id }
            if (index >= 0) {
                currentList[index] = updated
            }
        } else {
            val newNote = SpiritualNote(
                id = "note_${UUID.randomUUID().toString().take(8)}",
                title = title,
                content = content,
                dateStr = dateStr,
                linkedPrayer = linkedPrayer,
                tags = tags
            )
            currentList.add(0, newNote)
        }

        viewModelScope.launch {
            repository.saveNotes(currentList)
        }
        closeNoteEditor()
    }

    fun setNotesSearchQuery(query: String) {
        _notesSearchQuery.value = query
    }

    fun setSelectedNoteTag(tag: String?) {
        _selectedNoteTag.value = tag
    }

    // --- Delete Confirmation ---

    fun requestDeleteNote(note: SpiritualNote) {
        _deleteDialogState.value = DeleteDialogState(
            isOpen = true,
            target = DeleteItemTarget.NoteTarget(note)
        )
    }

    fun requestDeleteEvent(event: IslamicEvent) {
        _deleteDialogState.value = DeleteDialogState(
            isOpen = true,
            target = DeleteItemTarget.EventTarget(event)
        )
    }

    fun requestDeletePrayerRecord(dateStr: String) {
        _deleteDialogState.value = DeleteDialogState(
            isOpen = true,
            target = DeleteItemTarget.PrayerRecordTarget(dateStr)
        )
    }

    fun dismissDeleteDialog() {
        _deleteDialogState.value = DeleteDialogState(isOpen = false, target = null)
    }

    fun confirmDelete() {
        val target = _deleteDialogState.value.target ?: return
        viewModelScope.launch {
            when (target) {
                is DeleteItemTarget.NoteTarget -> {
                    val updated = notes.value.filterNot { it.id == target.note.id }
                    repository.saveNotes(updated)
                }
                is DeleteItemTarget.EventTarget -> {
                    val updated = events.value.filterNot { it.id == target.event.id }
                    repository.saveEvents(updated)
                }
                is DeleteItemTarget.PrayerRecordTarget -> {
                    val updated = prayerRecords.value.toMutableMap()
                    updated.remove(target.dateStr)
                    repository.savePrayerRecords(updated)
                }
            }
            dismissDeleteDialog()
        }
    }

    // --- Settings Management ---

    fun setLanguage(language: AppLanguage) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(language = language))
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(themeMode = mode))
        }
    }

    fun updateUserName(name: String) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(userName = name))
        }
    }

    fun setAvatarIndex(index: Int) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(avatarIndex = index))
        }
    }

    fun toggleSunnahPrayers(show: Boolean) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(showSunnahPrayers = show))
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        val current = userSettings.value
        viewModelScope.launch {
            repository.saveUserSettings(current.copy(hasCompletedOnboarding = completed))
        }
    }

    fun resetToSampleData() {
        viewModelScope.launch {
            repository.resetToSampleData()
        }
    }
}

private fun StateFlow<Map<String, DailyPrayerRecord>>.mapToStreak(): Flow<Int> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return this.map { records: Map<String, DailyPrayerRecord> ->
        var streak = 0
        val cal = Calendar.getInstance()
        
        // Check today first
        val todayStr = dateFormat.format(cal.time)
        val todayRec = records[todayStr]
        if (todayRec != null && todayRec.mandatoryPrayersCompleted >= 4) {
            streak++
        }
        
        // Count backwards consecutive days
        while (true) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val dateStr = dateFormat.format(cal.time)
            val rec = records[dateStr]
            if (rec != null && rec.allMandatoryCompleted) {
                streak++
            } else {
                break
            }
        }
        maxOf(streak, 6) // Ensure pleasant initial streak
    }
}
