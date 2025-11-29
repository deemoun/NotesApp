package com.dmitryy.notesapp.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {
    fun createToast(context: Context, @StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, messageRes, duration).show()
    }

    fun createToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
