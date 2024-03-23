package ru.vaihdass.aikataulus.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vaihdass.aikataulus.data.local.db.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun add(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(tasks: List<TaskEntity>)

    @Delete
    fun delete(task: TaskEntity)

    @Delete
    fun delete(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE deadline >= :from AND deadline <= :to")
    suspend fun deleteAllFromTo(from: Long, to: Long)

    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("DELETE FROM tasks WHERE isDone = 1")
    fun deleteAllDone()

    @Query("SELECT * FROM tasks WHERE saved = 0")
    fun getAllUnsaved(): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE deadline >= :from AND deadline <= :to")
    suspend fun getAllFromTo(from: Long, to: Long): List<TaskEntity>
}