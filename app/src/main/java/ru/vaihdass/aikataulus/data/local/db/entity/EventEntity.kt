package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val location: String? = null,
    val type: String? = null,
    val teacher: String? = null,
    @ColumnInfo("calendar_id") val calendarId: Int,
    @ColumnInfo("calendar_name") val calendarName: String,
    @ColumnInfo("date_from") val dateFrom: Date,
    @ColumnInfo("date_to") val dateTo: Date,


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventEntity

        if (subject != other.subject) return false
        if (location != other.location) return false
        if (type != other.type) return false
        if (teacher != other.teacher) return false
        if (calendarId != other.calendarId) return false
        if (calendarName != other.calendarName) return false
        if (dateFrom != other.dateFrom) return false
        if (dateTo != other.dateTo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = subject.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (teacher?.hashCode() ?: 0)
        result = 31 * result + calendarId
        result = 31 * result + calendarName.hashCode()
        result = 31 * result + dateFrom.hashCode()
        result = 31 * result + dateTo.hashCode()
        return result
    }
}