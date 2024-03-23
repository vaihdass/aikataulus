package ru.vaihdass.aikataulus.data.remote.pojo.aikataulus

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Event(
    var subject: String,
    var location: String? = null,
    var type: String? = null,
    var teacher: String? = null,
    @SerializedName("calendar_id") var calendarId: Int,
    @SerializedName("calendar_name") var calendarName: String,
    @SerializedName("date_from") var dateFrom: Date,
    @SerializedName("date_to") var dateTo: Date,
)