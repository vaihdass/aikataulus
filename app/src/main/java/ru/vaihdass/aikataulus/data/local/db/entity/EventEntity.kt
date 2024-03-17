package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ru.vaihdass.aikataulus.data.local.db.entity.converter.DateConverter
import java.util.Date

@TypeConverters(DateConverter::class)
@Entity(tableName = "events")
class EventEntity(
    @PrimaryKey val id: Int,
    val subject: String,
    val location: String? = null,
    val type: String? = null,
    val teacher: String? = null,
    @SerializedName("calendar_id") val calendarId: Int,
    @SerializedName("calendar_name") val calendarName: String,
    @SerializedName("date_from") val dateFrom: Date,
    @SerializedName("date_to") val dateTo: Date,
)