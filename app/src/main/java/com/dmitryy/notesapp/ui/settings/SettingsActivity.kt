package com.dmitryy.notesapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dmitryy.notesapp.NotesApplication
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.ui.login.LoginActivity
import com.dmitryy.notesapp.ui.theme.NotesAppTheme
import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.NavigationUtils
import com.dmitryy.notesapp.utils.ToastUtils

class SettingsActivity : ComponentActivity(), SettingsContract.View {

    private lateinit var presenter: SettingsContract.Presenter
    private var isPasscodeEnabled by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("SettingsActivity: onCreate")

        val database = (application as NotesApplication).database
        val repository = NotesRepository(database.noteDao())
        presenter = SettingsPresenter(repository, this)
        presenter.attachView(this)
        presenter.loadPasscodeState()

        setContent {
            NotesAppTheme {
                SettingsScreen(
                    onBack = { 
                        Logger.d("SettingsActivity: onBack")
                        finish() 
                    },
                    onClearCache = { 
                        Logger.d("SettingsActivity: onClearCache")
                        presenter.clearCache() 
                    },
                    onNukeDatabase = { 
                        Logger.d("SettingsActivity: onNukeDatabase")
                        presenter.nukeDatabase() 
                    },
                    onPasscodeToggle = { enabled ->
                        Logger.d("SettingsActivity: onPasscodeToggle - $enabled")
                        presenter.togglePasscode(enabled)
                    },
                    isPasscodeEnabled = isPasscodeEnabled
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("SettingsActivity: onDestroy")
        presenter.detachView()
    }

    override fun showCacheCleared() {
        Logger.d("SettingsActivity: showCacheCleared")
        ToastUtils.createToast(this, R.string.cache_cleared)
    }

    override fun showDatabaseNuked() {
        Logger.d("SettingsActivity: showDatabaseNuked")
        ToastUtils.createToast(this, R.string.database_nuked)
    }

    override fun showError(message: String) {
        Logger.e("SettingsActivity: showError - $message")
        ToastUtils.createToast(this, message)
    }

    override fun navigateToPasscodeSetup() {
        Logger.d("SettingsActivity: navigateToPasscodeSetup")
        NavigationUtils.navigateToLogin(this, isSetup = true)
        finish()
    }

    override fun updatePasscodeState(enabled: Boolean) {
        Logger.d("SettingsActivity: updatePasscodeState - $enabled")
        isPasscodeEnabled = enabled
    }
}
