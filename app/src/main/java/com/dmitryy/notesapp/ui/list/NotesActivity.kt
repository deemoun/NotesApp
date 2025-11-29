package com.dmitryy.notesapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.dmitryy.notesapp.NotesApplication
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.Note
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.ui.detail.NoteDetailActivity
import com.dmitryy.notesapp.ui.login.LoginActivity
import com.dmitryy.notesapp.ui.theme.NotesAppTheme
import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.NavigationUtils
import com.dmitryy.notesapp.utils.PreferencesManager
import com.dmitryy.notesapp.utils.ToastUtils

class NotesActivity : ComponentActivity(), NotesContract.View {

    private lateinit var presenter: NotesContract.Presenter
    private val notesState = mutableStateOf<List<Note>>(emptyList())
    private val showTrashBinState = mutableStateOf(false)

    private val importJsonLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            Logger.d("NotesActivity: importJsonLauncher - uri: $uri")
            try {
                val jsonContent = contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                    reader.readText()
                }
                if (jsonContent != null) {
                    Logger.d("NotesActivity: importJsonLauncher - content length: ${jsonContent.length}")
                    presenter.importNotes(jsonContent)
                    Logger.d("NotesActivity: importJsonLauncher - import successful")
                    ToastUtils.createToast(this, R.string.import_success)
                }
            } catch (e: Exception) {
                Logger.e("NotesActivity: importJsonLauncher - import failed", e)
                ToastUtils.createToast(this, getString(R.string.import_failed, e.message))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("NotesActivity: onCreate - START")

        val prefsManager = PreferencesManager(this)
        val isPasscodeEnabled = prefsManager.isPasscodeEnabled()
        val isLoggedIn = com.dmitryy.notesapp.utils.SessionManager.isLoggedIn

        Logger.d("NotesActivity: onCreate - passcodeEnabled: $isPasscodeEnabled, isLoggedIn: $isLoggedIn")

        if (isPasscodeEnabled && !isLoggedIn) {
            Logger.d("NotesActivity: onCreate - REDIRECTING to login (passcode required and not logged in)")
            NavigationUtils.navigateToLogin(this)
            finish()
            return
        }
        
        Logger.d("NotesActivity: onCreate - PROCEEDING to main screen (no passcode or already logged in)")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = NotesPresenter(repository)
        presenter.attachView(this)

        setContent {
            NotesAppTheme {
                NotesListScreen(
                    notes = notesState.value,
                    showTrashBin = showTrashBinState.value,
                    onNoteClick = { note -> presenter.onNoteClicked(note) },
                    onAddNoteClick = { presenter.onAddNoteClicked() },
                    onDeleteNote = { note -> presenter.onDeleteNote(note) },
                    onSearchQueryChanged = { query -> presenter.onSearchQueryChanged(query) },
                    onClearSearch = { presenter.onClearSearch() },
                    onAbout = { handleAbout() },
                    onExportJson = { handleExportJson() },
                    onImportJson = { handleImportJson() },
                    onTrashBin = { handleTrashBin() },
                    onSettings = { handleSettings() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.d("NotesActivity: onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("NotesActivity: onDestroy")
        if (::presenter.isInitialized) {
            presenter.detachView()
        }
    }

    override fun showNotes(notes: List<Note>) {
        Logger.d("NotesActivity: showNotes - count: ${notes.size}")
        notesState.value = notes
    }

    override fun showNoteDetail(noteId: Int) {
        Logger.d("NotesActivity: showNoteDetail - noteId: $noteId")
        NavigationUtils.navigateToNoteDetail(this, noteId)
    }

    override fun showAddNote() {
        Logger.d("NotesActivity: showAddNote")
        NavigationUtils.navigateToNoteDetail(this)
    }

    override fun updateTrashBinVisibility(isVisible: Boolean) {
        Logger.d("NotesActivity: updateTrashBinVisibility - isVisible: $isVisible")
        showTrashBinState.value = isVisible
    }

    override fun showError(message: String) {
        Logger.e("NotesActivity: showError - $message")
        ToastUtils.createToast(this, message)
    }

    private fun handleAbout() {
        Logger.d("NotesActivity: handleAbout")
        ToastUtils.createToast(this, R.string.about_message)
    }

    private fun handleExportJson() {
        Logger.d("NotesActivity: handleExportJson")
        try {
            val jsonContent = presenter.exportAllNotes()
            val shareIntent = com.dmitryy.notesapp.utils.FileUtils.saveAndShareJson(this, jsonContent)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.export_json)))
            Logger.d("NotesActivity: handleExportJson - export successful")
            ToastUtils.createToast(this, R.string.export_success)
        } catch (e: Exception) {
            Logger.e("NotesActivity: handleExportJson - export failed", e)
            ToastUtils.createToast(this, getString(R.string.export_failed, e.message))
        }
    }

    private fun handleImportJson() {
        Logger.d("NotesActivity: handleImportJson - launching file picker")
        importJsonLauncher.launch("application/json")
    }

    private fun handleTrashBin() {
        Logger.d("NotesActivity: handleTrashBin")
        NavigationUtils.navigateToTrashBin(this)
    }

    private fun handleSettings() {
        Logger.d("NotesActivity: handleSettings")
        NavigationUtils.navigateToSettings(this)
    }
}
