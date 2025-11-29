package com.dmitryy.notesapp.ui.trash

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface TrashBinContract {
    interface View : BaseView {
        fun showDeletedNotes(notes: List<Note>)
        fun showMessage(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadDeletedNotes()
        fun restoreNote(note: Note)
        fun restoreAll()
        fun emptyTrash()
        fun deleteForever(note: Note)
    }
}
