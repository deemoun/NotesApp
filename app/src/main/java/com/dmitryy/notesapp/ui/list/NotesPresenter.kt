package com.dmitryy.notesapp.ui.list

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class NotesPresenter(private val repository: NotesRepository) : NotesContract.Presenter, CoroutineScope {

    private var view: NotesContract.View? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: NotesContract.View) {
        Logger.d("NotesPresenter: attachView")
        this.view = view
        loadNotes()
    }

    override fun detachView() {
        Logger.d("NotesPresenter: detachView")
        view = null
        job.cancel()
    }

    private var currentQuery: String = ""
    private var searchJob: Job? = null

    override fun loadNotes() {
        Logger.d("NotesPresenter: loadNotes - query: '$currentQuery'")
        searchJob?.cancel()
        searchJob = launch {
            val flow = if (currentQuery.isEmpty()) {
                repository.allNotes
            } else {
                repository.searchNotes(currentQuery)
            }
            flow.collectLatest { notes ->
                Logger.d("NotesPresenter: loadNotes - received ${notes.size} notes")
                view?.showNotes(notes)
            }
        }

        launch {
            repository.deletedNotes.collectLatest { deletedNotes ->
                val hasDeletedNotes = deletedNotes.isNotEmpty()
                Logger.d("NotesPresenter: loadNotes - hasDeletedNotes: $hasDeletedNotes")
                view?.updateTrashBinVisibility(hasDeletedNotes)
            }
        }
    }

    override fun onNoteClicked(note: Note) {
        Logger.d("NotesPresenter: onNoteClicked - noteId: ${note.id}, title: '${note.title}'")
        view?.showNoteDetail(note.id)
    }

    override fun onAddNoteClicked() {
        Logger.d("NotesPresenter: onAddNoteClicked")
        view?.showAddNote()
    }

    override fun onDeleteNote(note: Note) {
        Logger.d("NotesPresenter: onDeleteNote - noteId: ${note.id}, title: '${note.title}'")
        launch {
            repository.softDelete(note.id)
        }
    }

    override fun onPinNote(note: Note) {
        Logger.d("NotesPresenter: onPinNote - noteId: ${note.id}, title: '${note.title}', current isPinned: ${note.isPinned}")
        launch {
            // Fetch the latest state from database to ensure we have the current pin status
            val currentNote = repository.getNoteById(note.id)
            if (currentNote != null) {
                val newPinState = !currentNote.isPinned
                Logger.d("NotesPresenter: onPinNote - toggling from ${currentNote.isPinned} to $newPinState")
                repository.togglePin(note.id, newPinState)
            } else {
                Logger.w("NotesPresenter: onPinNote - note not found in database")
            }
        }
    }

    override fun onSearchQueryChanged(query: String) {
        Logger.d("NotesPresenter: onSearchQueryChanged - query: '$query'")
        currentQuery = query
        loadNotes()
    }

    override fun onClearSearch() {
        Logger.d("NotesPresenter: onClearSearch")
        currentQuery = ""
        loadNotes()
    }

    override fun exportAllNotes(): String {
        Logger.d("NotesPresenter: exportAllNotes")
        return runBlocking {
            val notes = repository.allNotes.first()
            Logger.d("NotesPresenter: exportAllNotes - exporting ${notes.size} notes")
            com.dmitryy.notesapp.utils.FileUtils.exportNotesToJson(notes)
        }
    }

    override fun importNotes(jsonContent: String) {
        Logger.d("NotesPresenter: importNotes - content length: ${jsonContent.length}")
        launch {
            try {
                val notes = com.dmitryy.notesapp.utils.FileUtils.importNotesFromJson(jsonContent)
                Logger.d("NotesPresenter: importNotes - importing ${notes.size} notes")
                notes.forEach { note ->
                    repository.insert(note.copy(id = 0)) // Reset ID to auto-generate
                }
                Logger.d("NotesPresenter: importNotes - completed successfully")
            } catch (e: Exception) {
                Logger.e("NotesPresenter: importNotes - failed", e)
                throw e
            }
        }
    }
}
