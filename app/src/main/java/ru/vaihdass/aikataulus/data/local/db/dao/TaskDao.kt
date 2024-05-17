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

    @Query("UPDATE tasks SET id = :newId WHERE id = :oldId")
    fun updateId(oldId: String, newId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    fun delete(taskId: String)

    @Delete
    fun delete(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE deadline >= :from AND deadline <= :to")
    suspend fun deleteAllFromTo(from: Long, to: Long)

    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("DELETE FROM tasks WHERE isDone = 1")
    fun deleteAllDone()

    @Query("SELECT * FROM tasks WHERE saved = 0 AND deleted = 0 AND createdOffline = 0")
    fun getAllUnsaved(): List<TaskEntity>

    @Query("UPDATE tasks SET saved = 1 WHERE saved = 0 AND deleted = 0 AND createdOffline = 0")
    fun markAllAsSaved()

    @Query("SELECT * FROM tasks WHERE deleted = 1")
    fun getAllDeletedUnsaved(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE createdOffline = 1")
    fun getAllCreatedUnsaved(): List<TaskEntity>

    @Query("UPDATE tasks SET saved = 1, createdOffline = 0 WHERE createdOffline = 1")
    fun markAllCreatedOfflineAsSaved()

    @Query("DELETE FROM tasks WHERE deleted = 1")
    fun deleteAllDeleted()

    @Query("SELECT * FROM tasks WHERE deleted = 0")
    fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getById(taskId: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE deadline >= :from AND deadline <= :to")
    suspend fun getAllFromTo(from: Long, to: Long): List<TaskEntity>
}