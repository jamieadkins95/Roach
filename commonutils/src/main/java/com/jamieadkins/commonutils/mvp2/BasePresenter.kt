package com.jamieadkins.commonutils.mvp2

open class BasePresenter<V> {
    var view: V? = null

    open fun onAttach(newView: V) {
        view = newView
    }

    open fun onDetach() {
        view = null
    }
}