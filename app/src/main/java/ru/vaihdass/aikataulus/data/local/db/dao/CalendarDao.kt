package ru.vaihdass.aikataulus.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vaihdass.aikataulus.data.local.db.entity.CalendarEntity

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertAll(entities: List<CalendarEntity>)

    @Query("DELETE FROM calendars WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM calendars")
    fun deleteAll()

    @Query("SELECT * FROM calendars")
    fun getAll(): List<CalendarEntity>

    @Query("SELECT id FROM calendars")
    fun getAllId(): List<Int>
}