package com.example.namajtrackerapp.model

import kotlinx.serialization.Serializable

@Serializable
enum class PrayerType(
    val defaultTimeEn: String,
    val arabicName: String,
    val defaultOrder: Int
) {
    FAJR("05:15 AM", "الفجر", 1),
    DHUHR("01:15 PM", "الظهر", 2),
    ASR("04:30 PM", "العصر", 3),
    MAGHRIB("06:05 PM", "المغرب", 4),
    ISHA("07:45 PM", "العشاء", 5),
    
    // Voluntary / Sunnah prayers
    TAHAJJUD("04:00 AM", "التهجد", 0),
    DUHA("09:30 AM", "الضحى", 6),
    WITR("08:15 PM", "الوتر", 7);

    val isMandatory: Boolean
        get() = this in listOf(FAJR, DHUHR, ASR, MAGHRIB, ISHA)
}

@Serializable
enum class PrayerStatus {
    NOT_YET,
    ON_TIME,
    LATE,
    MISSED;

    val isCompleted: Boolean
        get() = this == ON_TIME || this == LATE
}

@Serializable
data class PrayerEntry(
    val prayerType: PrayerType,
    val status: PrayerStatus = PrayerStatus.NOT_YET,
    val loggedAtTime: String? = null,
    val note: String? = null
)

@Serializable
data class DailyPrayerRecord(
    val dateStr: String, // YYYY-MM-DD
    val prayers: Map<PrayerType, PrayerStatus> = emptyMap(),
    val prayerNotes: Map<PrayerType, String> = emptyMap(),
    val dailyReflection: String? = null
) {
    val completedCount: Int
        get() = prayers.values.count { it.isCompleted }

    val mandatoryPrayersCompleted: Int
        get() = PrayerType.entries
            .filter { it.isMandatory }
            .count { (prayers[it] ?: PrayerStatus.NOT_YET).isCompleted }

    val allMandatoryCompleted: Boolean
        get() = mandatoryPrayersCompleted == 5

    val onTimeCount: Int
        get() = prayers.values.count { it == PrayerStatus.ON_TIME }

    val lateCount: Int
        get() = prayers.values.count { it == PrayerStatus.LATE }

    val missedCount: Int
        get() = prayers.values.count { it == PrayerStatus.MISSED }
}
