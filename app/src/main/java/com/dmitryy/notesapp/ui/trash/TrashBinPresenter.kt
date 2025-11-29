package com.dmitryy.notesapp.ui.trash

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TrashBinPresenter(private val repository: NotesRepository) : TrashBinContract.Presenter, CoroutineScope {

    private var view: TrashBinContract.View? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: TrashBinContract.View) {
        this.view = view
        loadDeletedNotes()
    }

    override fun detachView() {
        view = null
        job.cancel()
    }

    override fun loadDeletedNotes() {
        launch {
            repository.deletedNotes.collectLatest { notes ->
                view?.showDeletedNotes(notes)
            }
        }
    }

    override fun restoreNote(note: Note) {
        launch {
            repository.restore(note.id)
            // View update happens via flow collection
        }
    }

    override fun restoreAll() {
        launch {
            repository.restoreAll()
        }
    }

    override fun emptyTrash() {
        launch {
            repository.emptyTrash()
        }
    }

    override fun deleteForever(note: Note) {
        launch {
            repository.delete(note)
        }
    }
}
