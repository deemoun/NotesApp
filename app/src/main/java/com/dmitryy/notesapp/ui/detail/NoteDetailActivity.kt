package com.dmitryy.notesapp.ui.detail

import android.content.Intent
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
import com.dmitryy.notesapp.utils.ToastUtils

class NoteDetailActivity : ComponentActivity(), NoteDetailContract.View {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private lateinit var presenter: NoteDetailContract.Presenter
    private val titleState = mutableStateOf("")
    private val contentState = mutableStateOf("")
    private val isPinnedState = mutableStateOf(false)
    private var showDelete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("NoteDetailActivity: onCreate")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = NoteDetailPresenter(repository)
        presenter.attachView(this)

        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)
        if (noteId != -1) {
            Logger.d("NoteDetailActivity: onCreate - loading note id: $noteId")
            presenter.loadNote(noteId)
            showDelete = true
        } else {
            Logger.d("NoteDetailActivity: onCreate - creating new note")
            showDelete = false
        }

        setContent {
            NotesAppTheme {
                NoteDetailScreen(
                    title = titleState.value,
                    content = contentState.value,
                    onTitleChange = { titleState.value = it },
                    onContentChange = { contentState.value = it },
                    onSave = {
                        Logger.d("NoteDetailActivity: onSave - title: '${titleState.value}', content length: ${contentState.value.length}")
                        presenter.saveNote(titleState.value, contentState.value)
                    },
                    onDelete = {
                        Logger.d("NoteDetailActivity: onDelete")
                        presenter.deleteNote()
                    },
                    onBack = {
                        Logger.d("NoteDetailActivity: onBack")
                        finish()
                    },
                    onExportPdf = {
                        Logger.d("NoteDetailActivity: onExportPdf")
                        presenter.onExportPdf(titleState.value, contentState.value)
                    },
                    showDelete = showDelete,
                    isPinned = isPinnedState.value,
                    onTogglePin = {
                        Logger.d("NoteDetailActivity: onTogglePin")
                        presenter.togglePin()
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("NoteDetailActivity: onDestroy")
        presenter.detachView()
    }

    override fun showNote(note: Note) {
        Logger.d("NoteDetailActivity: showNote - id: ${note.id}, title: '${note.title}'")
        titleState.value = note.title
        contentState.value = note.content
        showDelete = true
    }

    override fun close() {
        Logger.d("NoteDetailActivity: close")
        finish()
    }

    override fun showError(message: String) {
        Logger.e("NoteDetailActivity: showError - $message")
        ToastUtils.createToast(this, message)
    }

    override fun exportNoteToPdf(title: String, content: String) {
        Logger.d("NoteDetailActivity: exportNoteToPdf - title: '$title'")
        try {
            val shareIntent = com.dmitryy.notesapp.utils.FileUtils.generateAndSharePdf(this, title, content)
            Logger.d("NoteDetailActivity: exportNoteToPdf - PDF generated successfully")
            startActivity(Intent.createChooser(shareIntent, getString(R.string.export_note_as_pdf)))
        } catch (e: Exception) {
            Logger.e("NoteDetailActivity: exportNoteToPdf - failed", e)
            showError(getString(R.string.export_pdf_failed, e.message))
        }
    }

    override fun updatePinState(isPinned: Boolean) {
        Logger.d("NoteDetailActivity: updatePinState - isPinned: $isPinned")
        isPinnedState.value = isPinned
    }
}
