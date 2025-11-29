package com.dmitryy.notesapp.ui.url

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

class UrlNotesActivity : ComponentActivity(), UrlNotesContract.View {

    private lateinit var presenter: UrlNotesContract.Presenter
    private val notesState = mutableStateOf<List<Note>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("UrlNotesActivity: onCreate")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = UrlNotesPresenter(repository)
        presenter.attachView(this)

        setContent {
            NotesAppTheme {
                UrlNotesScreen(
                    notes = notesState.value,
                    onNoteClick = { note ->
                        NavigationUtils.navigateToNoteDetail(this, note.id)
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

    override fun showNotes(notes: List<Note>) {
        notesState.value = notes
    }

    override fun showError(message: String) {
        Logger.e("UrlNotesActivity: showError - $message")
        ToastUtils.createToast(this, message)
    }
}
