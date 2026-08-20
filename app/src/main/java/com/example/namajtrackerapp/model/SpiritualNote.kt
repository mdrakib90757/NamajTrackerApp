package com.example.namajtrackerapp.model

import kotlinx.serialization.Serializable

@Serializable
data class SpiritualNote(
    val id: String,
    val title: String,
    val content: String,
    val dateStr: String, // YYYY-MM-DD
    val linkedPrayer: PrayerType? = null,
    val linkedEventId: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
