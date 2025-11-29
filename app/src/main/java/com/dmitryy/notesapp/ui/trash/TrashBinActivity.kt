package com.dmitryy.notesapp.ui.trash

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.dmitryy.notesapp.NotesApplication
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.ui.theme.NotesAppTheme
import com.dmitryy.notesapp.utils.Logger

class TrashBinActivity : ComponentActivity(), TrashBinContract.View {

    private lateinit var presenter: TrashBinContract.Presenter
    private val notesState = mutableStateOf<List<Note>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("TrashBinActivity: onCreate")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = TrashBinPresenter(repository)
        presenter.attachView(this)

        setContent {
            NotesAppTheme {
                TrashBinScreen(
                    notes = notesState.value,
                    onRestoreNote = { note ->
                        presenter.restoreNote(note)
                        Toast.makeText(this, getString(R.string.note_restored), Toast.LENGTH_SHORT).show()
                    },
                    onDeleteForever = { note ->
                        presenter.deleteForever(note)
                        Toast.makeText(this, getString(R.string.delete_forever), Toast.LENGTH_SHORT).show()
                    },
                    onRestoreAll = {
                        presenter.restoreAll()
                        Toast.makeText(this, getString(R.string.notes_restored), Toast.LENGTH_SHORT).show()
                    },
                    onEmptyTrash = {
                        presenter.emptyTrash()
                        Toast.makeText(this, getString(R.string.trash_cleared), Toast.LENGTH_SHORT).show()
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

    override fun showDeletedNotes(notes: List<Note>) {
        notesState.value = notes
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
