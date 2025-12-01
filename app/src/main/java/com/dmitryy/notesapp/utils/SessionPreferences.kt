package com.dmitryy.notesapp.utils

import com.dmitryy.notesapp.ui.list.ViewMode

/**
 * Holds non-persistent, in-memory UI preferences for the current app session.
 *
 * These preferences survive configuration changes (e.g., rotations) within the
 * same process but are reset when the app process is killed.
 */
object SessionPreferences {
    private var _viewMode: ViewMode = ViewMode.LIST

    val viewMode: ViewMode
        get() {
            Logger.d("SessionPreferences: viewMode getter - $_viewMode")
            return _viewMode
        }

    fun setViewMode(mode: ViewMode) {
        Logger.d("SessionPreferences: setViewMode - changing from $_viewMode to $mode")
        _viewMode = mode
    }
}
