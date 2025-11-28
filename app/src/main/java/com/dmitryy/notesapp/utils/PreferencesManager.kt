package com.dmitryy.notesapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * PreferencesManager handles secure storage of passcode settings using EncryptedSharedPreferences.
 * Data is encrypted at rest using AES256-GCM encryption.
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences
    
    companion object {
        private const val PREFS_NAME = "DYNotesSecurePrefs"
        private const val KEY_PASSCODE_ENABLED = "passcode_enabled"
        private const val KEY_PASSCODE = "passcode"
    }
    
    init {
        Logger.d("PreferencesManager: init - creating encrypted shared preferences")
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            prefs = EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            Logger.d("PreferencesManager: init - encrypted shared preferences created successfully")
        } catch (e: Exception) {
            Logger.e("PreferencesManager: init - failed to create encrypted preferences", e)
            throw e
        }
    }
    
    fun isPasscodeEnabled(): Boolean {
        val enabled = prefs.getBoolean(KEY_PASSCODE_ENABLED, false)
        Logger.d("PreferencesManager: isPasscodeEnabled - $enabled")
        return enabled
    }
    
    fun setPasscodeEnabled(enabled: Boolean) {
        Logger.d("PreferencesManager: setPasscodeEnabled - $enabled")
        prefs.edit().putBoolean(KEY_PASSCODE_ENABLED, enabled).apply()
    }
    
    fun getPasscode(): String? {
        val passcode = prefs.getString(KEY_PASSCODE, null)
        Logger.d("PreferencesManager: getPasscode - ${if (passcode != null) "exists" else "null"}")
        return passcode
    }
    
    fun setPasscode(passcode: String) {
        Logger.d("PreferencesManager: setPasscode - setting 4-digit code (encrypted)")
        prefs.edit().putString(KEY_PASSCODE, passcode).apply()
    }
    
    fun clearPasscode() {
        Logger.d("PreferencesManager: clearPasscode - removing encrypted passcode")
        prefs.edit()
            .remove(KEY_PASSCODE)
            .putBoolean(KEY_PASSCODE_ENABLED, false)
            .apply()
    }
}
