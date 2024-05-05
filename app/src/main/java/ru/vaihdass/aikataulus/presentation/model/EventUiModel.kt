package ru.vaihdass.aikataulus.presentation.model

import java.io.Serializable
import java.util.Date

data class EventUiModel(
    val subject: String,
    val location: String? = null,
    val type: String? = null,
    val teacher: String? = null,
    val calendarId: Int,
    val calendarName: String,
    val dateFrom: Date,
    val dateTo: Date,
) : Serializable