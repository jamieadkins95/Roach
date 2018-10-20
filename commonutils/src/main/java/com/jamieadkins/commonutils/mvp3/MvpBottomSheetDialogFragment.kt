package com.jamieadkins.commonutils.mvp3

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jamieadkins.commonutils.mvp2.BasePresenter

abstract class MvpBottomSheetDialogFragment<V> : BottomSheetDialogFragment() {
    val presenter: BasePresenter<V> by lazy { setupPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this as V)
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    abstract fun setupPresenter(): BasePresenter<V>
}