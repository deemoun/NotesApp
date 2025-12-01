package com.dmitryy.notesapp.ui.pinned

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface PinnedNotesContract {
    interface View : BaseView {
        fun showPinnedNotes(notes: List<Note>)
    }

    interface Presenter : BasePresenter<View> {
        fun loadPinnedNotes()
        fun onNoteClicked(note: Note)
        fun unpinNote(note: Note)
        fun deleteNote(note: Note)
    }
}
