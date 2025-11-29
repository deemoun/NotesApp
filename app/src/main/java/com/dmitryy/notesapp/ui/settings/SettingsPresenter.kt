package com.dmitryy.notesapp.ui.settings

import android.content.Context
import com.dmitryy.notesapp.data.NotesRepository
import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class SettingsPresenter(
    private val repository: NotesRepository,
    private val context: Context
) : SettingsContract.Presenter, CoroutineScope {

    private var view: SettingsContract.View? = null
    private val job = Job()
    private val prefsManager = PreferencesManager(context)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: SettingsContract.View) {
        Logger.d("SettingsPresenter: attachView")
        this.view = view
    }

    override fun detachView() {
        Logger.d("SettingsPresenter: detachView")
        view = null
        job.cancel()
    }

    override fun clearCache() {
        Logger.d("SettingsPresenter: clearCache")
        launch(Dispatchers.IO) {
            try {
                val cacheSize = context.cacheDir.walkTopDown().sumOf { it.length() }
                Logger.d("SettingsPresenter: clearCache - cache size: $cacheSize bytes")
                context.cacheDir.deleteRecursively()
                Logger.d("SettingsPresenter: clearCache - completed successfully")
                launch(Dispatchers.Main) {
                    view?.showCacheCleared()
                }
            } catch (e: Exception) {
                Logger.e("SettingsPresenter: clearCache - failed", e)
                launch(Dispatchers.Main) {
                    view?.showError("Failed to clear cache: ${e.message}")
                }
            }
        }
    }

    override fun nukeDatabase() {
        Logger.d("SettingsPresenter: nukeDatabase")
        launch {
            try {
                repository.deleteAll()
                Logger.d("SettingsPresenter: nukeDatabase - completed successfully")
                view?.showDatabaseNuked()
            } catch (e: Exception) {
                Logger.e("SettingsPresenter: nukeDatabase - failed", e)
                view?.showError("Failed to nuke database: ${e.message}")
            }
        }
    }

    override fun togglePasscode(enabled: Boolean) {
        Logger.d("SettingsPresenter: togglePasscode - enabled: $enabled")
        if (enabled) {
            Logger.d("SettingsPresenter: togglePasscode - navigating to passcode setup")
            view?.navigateToPasscodeSetup()
        } else {
            Logger.d("SettingsPresenter: togglePasscode - clearing passcode and resetting session")
            prefsManager.clearPasscode()
            com.dmitryy.notesapp.utils.SessionManager.reset()
            view?.updatePasscodeState(false)
        }
    }

    override fun loadPasscodeState() {
        Logger.d("SettingsPresenter: loadPasscodeState")
        val enabled = prefsManager.isPasscodeEnabled()
        Logger.d("SettingsPresenter: loadPasscodeState - enabled: $enabled")
        view?.updatePasscodeState(enabled)
    }

    override fun loadDebugInfo() {
        Logger.d("SettingsPresenter: loadDebugInfo")
        val isRooted = com.dmitryy.notesapp.utils.SecurityUtils.isRooted()
        val hasEmulator = com.dmitryy.notesapp.utils.SecurityUtils.hasEmulator()
        val hasMagisk = com.dmitryy.notesapp.utils.SecurityUtils.hasMagisk()
        Logger.d("SettingsPresenter: loadDebugInfo - isRooted: $isRooted, hasEmulator: $hasEmulator, hasMagisk: $hasMagisk")
        view?.showDebugInfo(isRooted, hasEmulator, hasMagisk)
    }
}
