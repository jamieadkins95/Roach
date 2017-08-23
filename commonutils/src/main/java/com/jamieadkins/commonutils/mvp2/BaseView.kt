package com.jamieadkins.commonutils.mvp2

interface BaseView {
    fun setLoadingIndicator(loading: Boolean)

    fun showGenericErrorMessage()
}
