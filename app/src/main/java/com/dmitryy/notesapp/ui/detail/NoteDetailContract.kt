package com.dmitryy.notesapp.ui.detail

import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface NoteDetailContract {
    interface View : BaseView {
        fun showNote(note: Note)
        fun close()
        fun exportNoteToPdf(title: String, content: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadNote(id: Int)
        fun saveNote(title: String, content: String)
        fun deleteNote()
        fun onExportPdf(title: String, content: String)
    }
}
