package ru.vaihdass.aikataulus.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vaihdass.aikataulus.data.local.db.dao.CalendarDao
import ru.vaihdass.aikataulus.data.local.db.dao.CourseDao
import ru.vaihdass.aikataulus.data.local.db.dao.EventDao
import ru.vaihdass.aikataulus.data.local.db.dao.OrganizationDao
import ru.vaihdass.aikataulus.data.local.db.dao.TaskDao
import ru.vaihdass.aikataulus.data.local.db.entity.CalendarEntity
import ru.vaihdass.aikataulus.data.local.db.entity.CourseEntity
import ru.vaihdass.aikataulus.data.local.db.entity.EventEntity
import ru.vaihdass.aikataulus.data.local.db.entity.OrganizationEntity
import ru.vaihdass.aikataulus.data.local.db.entity.TaskEntity
import ru.vaihdass.aikataulus.data.local.db.entity.converter.DateConverter

@Database(
    entities = [
        TaskEntity::class,
        EventEntity::class,
        OrganizationEntity::class,
        CourseEntity::class,
        CalendarEntity::class,
    ],
    version = 1,
)
@TypeConverters(DateConverter::class)
abstract class AikataulusDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
    abstract val taskDao: TaskDao
    abstract val organizationDao: OrganizationDao
    abstract val courseDao: CourseDao
    abstract val calendarDao: CalendarDao
}