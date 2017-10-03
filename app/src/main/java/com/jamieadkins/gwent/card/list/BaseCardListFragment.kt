package com.jamieadkins.gwent.card.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseFragment

/**
 * UI fragment that shows a list of the users decks.
 */

abstract class BaseCardListFragment<T : CardsContract.View> : BaseFragment<T>(), CardsContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(layoutId, container, false)
        setupViews(rootView)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        setupFilterMenu(menu, inflater)
    }

    open val layoutId: Int = R.layout.fragment_card_list
}
