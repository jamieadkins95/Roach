package com.jamieadkins.commonutils.mvp3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.DisplayMetrics
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

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return px
    }
}