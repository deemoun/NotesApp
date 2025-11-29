package com.dmitryy.notesapp.ui.pinned

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.dmitryy.notesapp.NotesApplication
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.ui.theme.NotesAppTheme
import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.NavigationUtils
import com.dmitryy.notesapp.utils.ToastUtils

class PinnedNotesActivity : ComponentActivity(), PinnedNotesContract.View {

    private lateinit var presenter: PinnedNotesContract.Presenter
    private val notesState = mutableStateOf<List<Note>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("PinnedNotesActivity: onCreate")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = PinnedNotesPresenter(repository)
        presenter.attachView(this)

        setContent {
            NotesAppTheme {
                PinnedNotesScreen(
                    notes = notesState.value,
                    onNoteClick = { note ->
                        NavigationUtils.navigateToNoteDetail(this, note.id)
                    },
                    onUnpinNote = { note ->
                        presenter.unpinNote(note)
                    },
                    onBack = { finish() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showPinnedNotes(notes: List<Note>) {
        notesState.value = notes
    }

    override fun showError(message: String) {
        Logger.e("PinnedNotesActivity: showError - $message")
        ToastUtils.createToast(this, message)
    }
}
