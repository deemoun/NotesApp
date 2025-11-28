package com.dmitryy.notesapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dmitryy.notesapp.R
import com.dmitryy.notesapp.ui.list.NotesActivity
import com.dmitryy.notesapp.ui.theme.NotesAppTheme
import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.PreferencesManager

class LoginActivity : ComponentActivity(), LoginContract.View {

    companion object {
        const val EXTRA_IS_SETUP = "extra_is_setup"
    }

    private lateinit var presenter: LoginContract.Presenter
    private lateinit var prefsManager: PreferencesManager
    private var currentMode by mutableStateOf(LoginMode.VALIDATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("LoginActivity: onCreate - START")

        prefsManager = PreferencesManager(this)
        val isSetupMode = intent.getBooleanExtra(EXTRA_IS_SETUP, false)
        
        Logger.d("LoginActivity: onCreate - isSetupMode: $isSetupMode")
        presenter = LoginPresenter(prefsManager, isSetupMode)
        presenter.attachView(this)
        presenter.checkMode()

        setContent {
            NotesAppTheme {
                LoginScreen(
                    mode = currentMode,
                    onPasscodeEntered = { code ->
                        Logger.d("LoginActivity: onPasscodeEntered - mode: $currentMode, code length: ${code.length}")
                        when (currentMode) {
                            LoginMode.SETUP -> presenter.setupPasscode(code)
                            LoginMode.CONFIRM -> presenter.confirmPasscode(code)
                            LoginMode.VALIDATE -> presenter.validatePasscode(code)
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("LoginActivity: onDestroy")
        presenter.detachView()
    }

    override fun navigateToMain() {
        Logger.d("LoginActivity: navigateToMain - setting session logged in and navigating")
        com.dmitryy.notesapp.utils.SessionManager.setLoggedIn(true)
        val intent = Intent(this, NotesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun showSetupMode() {
        Logger.d("LoginActivity: showSetupMode")
        currentMode = LoginMode.SETUP
    }

    override fun showValidateMode() {
        Logger.d("LoginActivity: showValidateMode")
        currentMode = LoginMode.VALIDATE
    }

    override fun showConfirmMode() {
        Logger.d("LoginActivity: showConfirmMode")
        currentMode = LoginMode.CONFIRM
    }

    override fun showError(message: String) {
        Logger.e("LoginActivity: showError - $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
