package com.dmitryy.notesapp.ui.pinned

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PinnedNotesPresenter(private val repository: NotesRepository) : PinnedNotesContract.Presenter, CoroutineScope {

    private var view: PinnedNotesContract.View? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: PinnedNotesContract.View) {
        Logger.d("PinnedNotesPresenter: attachView")
        this.view = view
        loadPinnedNotes()
    }

    override fun detachView() {
        Logger.d("PinnedNotesPresenter: detachView")
        view = null
        job.cancel()
    }

    override fun loadPinnedNotes() {
        Logger.d("PinnedNotesPresenter: loadPinnedNotes")
        launch {
            repository.pinnedNotes.collect { notes ->
                Logger.d("PinnedNotesPresenter: loadPinnedNotes - received ${notes.size} pinned notes")
                view?.showPinnedNotes(notes)
            }
        }
    }

    override fun onNoteClicked(note: Note) {
        Logger.d("PinnedNotesPresenter: onNoteClicked - noteId: ${note.id}")
        // Navigation handled by activity
    }

    override fun unpinNote(note: Note) {
        Logger.d("PinnedNotesPresenter: unpinNote - noteId: ${note.id}")
        launch {
            repository.togglePin(note.id, false)
        }
    }

    override fun deleteNote(note: Note) {
        Logger.d("PinnedNotesPresenter: deleteNote - noteId: ${note.id}")
        launch {
            repository.softDelete(note.id)
        }
    }
}
