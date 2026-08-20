package com.example.namajtrackerapp.model

import kotlinx.serialization.Serializable

@Serializable
enum class EventCategory {
    RAMADAN,
    EID,
    SUNNAH_FAST,
    SPECIAL_NIGHT,
    PERSONAL
}

@Serializable
data class IslamicEvent(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val dateStr: String, // YYYY-MM-DD
    val hijriDateEn: String = "",
    val hijriDateBn: String = "",
    val noteEn: String = "",
    val noteBn: String = "",
    val isRecurringYearly: Boolean = true,
    val category: EventCategory = EventCategory.SPECIAL_NIGHT,
    val createdAt: Long = System.currentTimeMillis()
)
