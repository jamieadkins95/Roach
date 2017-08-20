package com.jamieadkins.gwent.card.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.card.CardFilter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * UI fragment that shows a list of the users decks.
 */

abstract class BaseCardListFragment : BaseFragment(), CardsContract.View {
    private var mCardsPresenter: CardsContract.Presenter? = null

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

    open val layoutId: Int
        get() = R.layout.fragment_card_list

    override fun onStart() {
        super.onStart()
        onLoadData()
    }

    override fun onCardFilterUpdated() {
        recyclerViewAdapter.clear()
        onLoadData()
    }

    override fun onLoadData() {
        super.onLoadData()
        onLoadCardData()
    }

    fun onLoadCardData() {
        mCardsPresenter?.let {
            it.getCards(cardFilter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }
    }

    override fun setPresenter(presenter: CardsContract.Presenter) {
        mCardsPresenter = presenter
    }
}
