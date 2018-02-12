package com.jamieadkins.commonutils.mvp2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class MvpActivity<V> : AppCompatActivity() {
    var presenter: BasePresenter<V>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onAttach(this as V)
    }

    override fun onStop() {
        super.onStop()
        presenter?.onDetach()
    }

    abstract fun setupPresenter()
}