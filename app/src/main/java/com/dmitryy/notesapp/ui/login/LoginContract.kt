package com.dmitryy.notesapp.ui.login

import com.dmitryy.notesapp.ui.base.BasePresenter
import com.dmitryy.notesapp.ui.base.BaseView

interface LoginContract {
    interface View : BaseView {
        fun navigateToMain()
        fun showSetupMode()
        fun showValidateMode()
        fun showConfirmMode()
    }

    interface Presenter : BasePresenter<View> {
        fun checkMode()
        fun setupPasscode(code: String)
        fun confirmPasscode(code: String)
        fun validatePasscode(code: String)
    }
}
