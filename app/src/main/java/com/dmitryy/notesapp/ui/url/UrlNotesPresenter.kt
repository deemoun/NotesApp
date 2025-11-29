package com.dmitryy.notesapp.ui.url

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UrlNotesPresenter(private val repository: NotesRepository) : UrlNotesContract.Presenter, CoroutineScope {

    private var view: UrlNotesContract.View? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: UrlNotesContract.View) {
        this.view = view
        loadNotes()
    }

    override fun detachView() {
        view = null
        job.cancel()
    }

    override fun loadNotes() {
        Logger.d("UrlNotesPresenter: loadNotes")
        launch {
            repository.notesWithUrls.collect { notes ->
                Logger.d("UrlNotesPresenter: loadNotes - received ${notes.size} notes with URLs")
                view?.showNotes(notes)
            }
        }
    }

    override fun onNoteClicked(note: Note) {
        Logger.d("UrlNotesPresenter: onNoteClicked - noteId: ${note.id}")
        // Navigation is handled by the Activity/View
    }
}
