package com.dmitryy.notesapp.ui.detail

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NoteDetailPresenter(private val repository: NotesRepository) : NoteDetailContract.Presenter, CoroutineScope {

    private var view: NoteDetailContract.View? = null
    private val job = Job()
    private var currentNote: Note? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: NoteDetailContract.View) {
        Logger.d("NoteDetailPresenter: attachView")
        this.view = view
    }

    override fun detachView() {
        Logger.d("NoteDetailPresenter: detachView")
        view = null
        job.cancel()
    }

    override fun loadNote(id: Int) {
        Logger.d("NoteDetailPresenter: loadNote - noteId: $id")
        launch {
            val note = repository.getNoteById(id)
            if (note != null) {
                Logger.d("NoteDetailPresenter: loadNote - note found: '${note.title}'")
                currentNote = note
                view?.showNote(note)
            } else {
                Logger.w("NoteDetailPresenter: loadNote - note not found for id: $id")
            }
        }
    }

    override fun saveNote(title: String, content: String) {
        Logger.d("NoteDetailPresenter: saveNote - title: '$title', content length: ${content.length}")
        if (title.isBlank()) {
            Logger.w("NoteDetailPresenter: saveNote - title is blank, aborting")
            return
        }

        launch {
            val note = currentNote
            if (note != null) {
                Logger.d("NoteDetailPresenter: saveNote - updating existing note id: ${note.id}")
                val updatedNote = note.copy(title = title, content = content)
                repository.update(updatedNote)
            } else {
                Logger.d("NoteDetailPresenter: saveNote - creating new note")
                val newNote = Note(
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis()
                )
                repository.insert(newNote)
            }
            Logger.d("NoteDetailPresenter: saveNote - completed successfully")
            view?.close()
        }
    }

    override fun deleteNote() {
        Logger.d("NoteDetailPresenter: deleteNote")
        launch {
            currentNote?.let { note ->
                Logger.d("NoteDetailPresenter: deleteNote - deleting note id: ${note.id}, title: '${note.title}'")
                repository.delete(note)
                view?.close()
            } ?: Logger.w("NoteDetailPresenter: deleteNote - no current note to delete")
        }
    }

    override fun onExportPdf(title: String, content: String) {
        Logger.d("NoteDetailPresenter: onExportPdf - title: '$title', content length: ${content.length}")
        view?.exportNoteToPdf(title, content)
    }
}
