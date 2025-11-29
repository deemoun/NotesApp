package com.dmitryy.notesapp.utils

import android.content.Context
import android.content.Intent
import com.dmitryy.notesapp.ui.detail.NoteDetailActivity
import com.dmitryy.notesapp.ui.list.NotesActivity
import com.dmitryy.notesapp.ui.login.LoginActivity
import com.dmitryy.notesapp.ui.settings.SettingsActivity
import com.dmitryy.notesapp.ui.trash.TrashBinActivity

object NavigationUtils {
    
    fun navigateToNoteDetail(context: Context, noteId: Int? = null) {
        val intent = Intent(context, NoteDetailActivity::class.java)
        noteId?.let { intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, it) }
        context.startActivity(intent)
    }
    
    fun navigateToTrashBin(context: Context) {
        val intent = Intent(context, TrashBinActivity::class.java)
        context.startActivity(intent)
    }
    
    fun navigateToSettings(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }
    
    fun navigateToLogin(context: Context, isSetup: Boolean = false, clearStack: Boolean = false) {
        val intent = Intent(context, LoginActivity::class.java)
        if (isSetup) {
            intent.putExtra(LoginActivity.EXTRA_IS_SETUP, true)
        }
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
    
    fun navigateToNotes(context: Context, clearStack: Boolean = false) {
        val intent = Intent(context, NotesActivity::class.java)
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}
