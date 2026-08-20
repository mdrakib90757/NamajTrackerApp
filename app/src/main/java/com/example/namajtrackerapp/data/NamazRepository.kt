package com.example.namajtrackerapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.namajtrackerapp.model.AppLanguage
import com.example.namajtrackerapp.model.AppThemeMode
import com.example.namajtrackerapp.model.DailyPrayerRecord
import com.example.namajtrackerapp.model.IslamicEvent
import com.example.namajtrackerapp.model.SpiritualNote
import com.example.namajtrackerapp.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.text.get

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "namaz_tracker_prefs")

class NamazRepository(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val AVATAR_INDEX = intPreferencesKey("avatar_index")
        val LANGUAGE = stringPreferencesKey("language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val SHOW_SUNNAH = booleanPreferencesKey("show_sunnah")
        
        val PRAYER_RECORDS_JSON = stringPreferencesKey("prayer_records_json")
        val EVENTS_JSON = stringPreferencesKey("events_json")
        val NOTES_JSON = stringPreferencesKey("notes_json")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        val userName = prefs[PreferencesKeys.USER_NAME] ?: "Al-Abid"
        val avatarIndex = prefs[PreferencesKeys.AVATAR_INDEX] ?: 0
        val langStr = prefs[PreferencesKeys.LANGUAGE] ?: AppLanguage.ENGLISH.name
        val themeStr = prefs[PreferencesKeys.THEME_MODE] ?: AppThemeMode.SYSTEM.name
        val hasCompletedOnboarding = prefs[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false
        val showSunnah = prefs[PreferencesKeys.SHOW_SUNNAH] ?: true

        val language = try {
            AppLanguage.valueOf(langStr)
        } catch (e: Exception) {
            AppLanguage.ENGLISH
        }

        val themeMode = try {
            AppThemeMode.valueOf(themeStr)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }

        UserSettings(
            userName = userName,
            avatarIndex = avatarIndex,
            language = language,
            themeMode = themeMode,
            hasCompletedOnboarding = hasCompletedOnboarding,
            showSunnahPrayers = showSunnah
        )
    }

    val prayerRecordsFlow: Flow<Map<String, DailyPrayerRecord>> = context.dataStore.data.map { prefs ->
        val jsonStr = prefs[PreferencesKeys.PRAYER_RECORDS_JSON]
        if (jsonStr.isNullOrEmpty()) {
            SampleDataProvider.getInitialPrayerRecords()
        } else {
            try {
                json.decodeFromString<Map<String, DailyPrayerRecord>>(jsonStr)
            } catch (e: Exception) {
                SampleDataProvider.getInitialPrayerRecords()
            }
        }
    }

    val eventsFlow: Flow<List<IslamicEvent>> = context.dataStore.data.map { prefs ->
        val jsonStr = prefs[PreferencesKeys.EVENTS_JSON]
        if (jsonStr.isNullOrEmpty()) {
            SampleDataProvider.getInitialEvents()
        } else {
            try {
                json.decodeFromString<List<IslamicEvent>>(jsonStr)
            } catch (e: Exception) {
                SampleDataProvider.getInitialEvents()
            }
        }
    }

    val notesFlow: Flow<List<SpiritualNote>> = context.dataStore.data.map { prefs ->
        val jsonStr = prefs[PreferencesKeys.NOTES_JSON]
        if (jsonStr.isNullOrEmpty()) {
            SampleDataProvider.getInitialNotes()
        } else {
            try {
                json.decodeFromString<List<SpiritualNote>>(jsonStr)
            } catch (e: Exception) {
                SampleDataProvider.getInitialNotes()
            }
        }
    }

    suspend fun saveUserSettings(settings: UserSettings) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.USER_NAME] = settings.userName
            prefs[PreferencesKeys.AVATAR_INDEX] = settings.avatarIndex
            prefs[PreferencesKeys.LANGUAGE] = settings.language.name
            prefs[PreferencesKeys.THEME_MODE] = settings.themeMode.name
            prefs[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = settings.hasCompletedOnboarding
            prefs[PreferencesKeys.SHOW_SUNNAH] = settings.showSunnahPrayers
        }
    }

    suspend fun savePrayerRecords(records: Map<String, DailyPrayerRecord>) {
        context.dataStore.edit { prefs ->
            val jsonStr = json.encodeToString(records)
            prefs[PreferencesKeys.PRAYER_RECORDS_JSON] = jsonStr
        }
    }

    suspend fun saveEvents(events: List<IslamicEvent>) {
        context.dataStore.edit { prefs ->
            val jsonStr = json.encodeToString(events)
            prefs[PreferencesKeys.EVENTS_JSON] = jsonStr
        }
    }

    suspend fun saveNotes(notes: List<SpiritualNote>) {
        context.dataStore.edit { prefs ->
            val jsonStr = json.encodeToString(notes)
            prefs[PreferencesKeys.NOTES_JSON] = jsonStr
        }
    }

    suspend fun resetToSampleData() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.PRAYER_RECORDS_JSON] = json.encodeToString(SampleDataProvider.getInitialPrayerRecords())
            prefs[PreferencesKeys.EVENTS_JSON] = json.encodeToString(SampleDataProvider.getInitialEvents())
            prefs[PreferencesKeys.NOTES_JSON] = json.encodeToString(SampleDataProvider.getInitialNotes())
        }
    }
}
