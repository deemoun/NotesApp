package com.dmitryy.notesapp.ui.url

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface UrlNotesContract {
    interface View : BaseView {
        fun showNotes(notes: List<Note>)
    }

    interface Presenter : BasePresenter<View> {
        fun loadNotes()
        fun onNoteClicked(note: Note)
    }
}
