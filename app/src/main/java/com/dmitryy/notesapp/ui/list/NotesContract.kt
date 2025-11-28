package com.dmitryy.notesapp.ui.list

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface NotesContract {
    interface View : BaseView {
        fun showNotes(notes: List<Note>)
        fun showNoteDetail(noteId: Int)
        fun showAddNote()
    }

    interface Presenter : BasePresenter<View> {
        fun loadNotes()
        fun onNoteClicked(note: Note)
        fun onAddNoteClicked()
        fun onDeleteNote(note: Note)
        fun onSearchQueryChanged(query: String)
        fun onClearSearch()
        fun exportAllNotes(): String
        fun importNotes(jsonContent: String)
    }
}
