package com.dmitryy.notesapp.utils

import android.util.Log
import com.dmitryy.notesapp.BuildConfig

object Logger {
    private const val TAG = "DYNotes"

    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    fun i(message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, message)
        }
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(TAG, message, throwable)
            } else {
                Log.e(TAG, message)
            }
        }
    }

    fun v(message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, message)
        }
    }
}
