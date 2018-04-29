package com.jamieadkins.commonutils.mvp3

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.jamieadkins.commonutils.mvp2.BasePresenter

abstract class MvpFragment<V> : Fragment() {
    val presenter: BasePresenter<V> by lazy { setupPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    abstract fun setupPresenter(): BasePresenter<V>
}