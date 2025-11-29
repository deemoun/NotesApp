package com.dmitryy.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isDeleted = 1 ORDER BY createdAt DESC")
    fun getDeletedNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE (isDeleted = 0) AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') ORDER BY createdAt DESC")
    fun searchNotes(query: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Int): Note?

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE notes SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)

    @Query("UPDATE notes SET isDeleted = 0 WHERE id = :id")
    suspend fun restore(id: Int)

    @Query("UPDATE notes SET isDeleted = 0 WHERE isDeleted = 1")
    suspend fun restoreAll()

    @Query("DELETE FROM notes WHERE isDeleted = 1")
    suspend fun emptyTrash()

    @Query("SELECT * FROM notes WHERE isPinned = 1 AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getPinnedNotes(): Flow<List<Note>>

    @Query("UPDATE notes SET isPinned = :isPinned WHERE id = :noteId")
    suspend fun togglePin(noteId: Int, isPinned: Boolean)

    @Query("SELECT * FROM notes WHERE isDeleted = 0 AND (title LIKE '%http%' OR content LIKE '%http%') ORDER BY createdAt DESC")
    fun getNotesWithUrls(): Flow<List<Note>>

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
