package com.dmitryy.notesapp.ui.settings

import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface SettingsContract {
    interface View : BaseView {
        fun showCacheCleared()
        fun showDatabaseNuked()
        fun navigateToPasscodeSetup()
        fun updatePasscodeState(enabled: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun clearCache()
        fun nukeDatabase()
        fun togglePasscode(enabled: Boolean)
        fun loadPasscodeState()
    }
}
