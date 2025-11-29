package com.dmitryy.notesapp.data

import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAll()

    fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query)
    }

    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getById(id)
    }

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    suspend fun deleteAll() {
        noteDao.deleteAll()
    }

    val deletedNotes: Flow<List<Note>> = noteDao.getDeletedNotes()

    suspend fun softDelete(id: Int) {
        noteDao.softDelete(id)
    }

    suspend fun restore(id: Int) {
        noteDao.restore(id)
    }

    suspend fun restoreAll() {
        noteDao.restoreAll()
    }

    suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }

    val pinnedNotes: Flow<List<Note>> = noteDao.getPinnedNotes()

    suspend fun togglePin(noteId: Int, isPinned: Boolean) {
        noteDao.togglePin(noteId, isPinned)
    }

    val notesWithUrls: Flow<List<Note>> = noteDao.getNotesWithUrls()
}
