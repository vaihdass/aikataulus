package ru.vaihdass.aikataulus.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vaihdass.aikataulus.data.local.db.dao.EventDao
import ru.vaihdass.aikataulus.data.local.db.dao.TaskDao
import ru.vaihdass.aikataulus.data.local.db.entity.EventEntity
import ru.vaihdass.aikataulus.data.local.db.entity.TaskEntity
import ru.vaihdass.aikataulus.data.local.db.entity.converter.DateConverter

@Database(
    entities = [
        TaskEntity::class,
        EventEntity::class,
    ],
    version = 1,
)
@TypeConverters(DateConverter::class)
abstract class AikataulusDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
    abstract val taskDao: TaskDao
}