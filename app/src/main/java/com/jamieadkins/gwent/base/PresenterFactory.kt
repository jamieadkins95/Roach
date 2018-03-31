package com.jamieadkins.gwent.base

import com.jamieadkins.commonutils.mvp2.BasePresenter

object PresenterFactory {

    private val presenters = mutableMapOf<String, BasePresenter<*>>()

    fun <V> getPresenter(tag: String, default: () -> BasePresenter<V>): BasePresenter<V> {
        return presenters.getOrPut(tag, default) as BasePresenter<V>
    }
}