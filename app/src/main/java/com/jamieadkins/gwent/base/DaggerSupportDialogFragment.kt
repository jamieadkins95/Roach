package com.jamieadkins.gwent.base

import android.content.Context

import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection

/**
 * A [DialogFragment] that injects its members in [.onAttach].
 * Note that when this fragment gets reattached, its members will be injected again.
 */
abstract class DaggerSupportDialogFragment : DialogFragment() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}