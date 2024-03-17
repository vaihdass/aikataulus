package ru.vaihdass.aikataulus.data.remote.pojo

import com.google.gson.annotations.SerializedName
import java.util.Date

class Event(
    val subject: String,
    val location: String? = null,
    val type: String? = null,
    val teacher: String? = null,
    @SerializedName("calendar_id") val calendarId: Int,
    @SerializedName("calendar_name") val calendarName: String,
    @SerializedName("date_from") val dateFrom: Date,
    @SerializedName("date_to") val dateTo: Date,
)