package com.dmitryy.notesapp.ui.login

import com.dmitryy.notesapp.utils.Logger
import com.dmitryy.notesapp.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LoginPresenter(
    private val prefsManager: PreferencesManager,
    private val isSetupMode: Boolean
) : LoginContract.Presenter, CoroutineScope {

    private var view: LoginContract.View? = null
    private val job = Job()
    private var tempPasscode: String? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun attachView(view: LoginContract.View) {
        Logger.d("LoginPresenter: attachView - isSetupMode: $isSetupMode")
        this.view = view
    }

    override fun detachView() {
        Logger.d("LoginPresenter: detachView")
        view = null
        job.cancel()
    }

    override fun checkMode() {
        Logger.d("LoginPresenter: checkMode - isSetupMode: $isSetupMode")
        if (isSetupMode) {
            view?.showSetupMode()
        } else {
            view?.showValidateMode()
        }
    }

    override fun setupPasscode(code: String) {
        Logger.d("LoginPresenter: setupPasscode - code length: ${code.length}")
        if (code.length != 4) {
            Logger.w("LoginPresenter: setupPasscode - invalid code length")
            view?.showError("Passcode must be 4 digits")
            return
        }
        
        tempPasscode = code
        Logger.d("LoginPresenter: setupPasscode - showing confirm mode")
        view?.showConfirmMode()
    }

    override fun confirmPasscode(code: String) {
        Logger.d("LoginPresenter: confirmPasscode - code length: ${code.length}")
        if (code != tempPasscode) {
            Logger.w("LoginPresenter: confirmPasscode - passcodes do not match")
            view?.showError("Passcodes do not match")
            tempPasscode = null
            view?.showSetupMode()
            return
        }
        
        Logger.d("LoginPresenter: confirmPasscode - passcodes match, saving")
        prefsManager.setPasscode(code)
        prefsManager.setPasscodeEnabled(true)
        view?.navigateToMain()
    }

    override fun validatePasscode(code: String) {
        Logger.d("LoginPresenter: validatePasscode - code length: ${code.length}")
        val savedPasscode = prefsManager.getPasscode()
        
        if (code == savedPasscode) {
            Logger.d("LoginPresenter: validatePasscode - correct passcode")
            view?.navigateToMain()
        } else {
            Logger.w("LoginPresenter: validatePasscode - incorrect passcode")
            view?.showError("Incorrect passcode")
        }
    }
}
