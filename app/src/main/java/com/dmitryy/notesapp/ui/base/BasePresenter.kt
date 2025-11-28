package com.dmitryy.notesapp.ui.base

interface BasePresenter<T> {
    fun attachView(view: T)
    fun detachView()
}
