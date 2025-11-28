package com.dmitryy.notesapp.utils

/**
 * SessionManager manages the in-memory login state for the current app session.
 * This state is reset when the app process is killed.
 * 
 * Flow:
 * 1. Cold start: isLoggedIn = false
 * 2. User logs in successfully: isLoggedIn = true
 * 3. App backgrounded (process alive): isLoggedIn remains true
 * 4. App killed (process death): isLoggedIn resets to false on next launch
 */
object SessionManager {
    private var _isLoggedIn: Boolean = false
    
    val isLoggedIn: Boolean
        get() {
            Logger.d("SessionManager: isLoggedIn getter - $_isLoggedIn")
            return _isLoggedIn
        }

    fun setLoggedIn(loggedIn: Boolean) {
        Logger.d("SessionManager: setLoggedIn - changing from $_isLoggedIn to $loggedIn")
        _isLoggedIn = loggedIn
    }
    
    fun reset() {
        Logger.d("SessionManager: reset - clearing session")
        _isLoggedIn = false
    }
}
