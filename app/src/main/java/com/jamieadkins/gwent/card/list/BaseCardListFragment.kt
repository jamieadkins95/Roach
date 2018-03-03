package com.jamieadkins.gwent.card.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.commonutils.ui.RecyclerViewItem

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.model.GwentCard
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * UI fragment that shows a list of the users decks.
 */

abstract class BaseCardListFragment<T : CardsContract.View> : BaseFragment<T>(), CardsContract.View {

    lateinit var emptyView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        emptyView = rootView.findViewById(R.id.empty)
        setupViews(rootView)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        setupFilterMenu(menu, inflater)
    }

    override fun showCards(cards: MutableList<GwentCard>) {
        if (cards.size > 0) {
            context?.let { context ->
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val locale = preferences.getString(context.getString(R.string.pref_locale_key), context.getString(R.string.default_locale))
                val items = mutableListOf<RecyclerViewItem>()
                items.addAll(cards.sortedBy { it.name[locale] })
                showItems(items)

                emptyView.visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE

                // Have to post this since we have to wait for diff util to finish.
                mRecyclerView.post({
                    mRecyclerView.scrollToPosition(0)
                })
            }
        } else {
            emptyView.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        }

    }

    override fun showEmptyView() {

    }

    open val layoutId: Int = R.layout.fragment_card_list
}
