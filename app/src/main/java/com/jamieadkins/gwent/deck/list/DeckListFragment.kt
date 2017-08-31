package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.commonutils.mvp2.MvpFragment

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.commonutils.ui.SubHeader
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.base.BaseObserver
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.card.detail.DetailActivity
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.FirebaseUtils
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * UI fragment that shows a list of the users decks.
 */

class DeckListFragment : BaseFragment<DecksContract.View>(), DecksContract.View {

    // Set up to show user decks by default.
    private var mPublicDecks = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mPublicDecks = savedInstanceState.getBoolean(STATE_PUBLIC_DECKS)
        }

        if (!mPublicDecks) {
            activity.title = getString(R.string.my_decks)
        } else {
            activity.title = getString(R.string.public_decks)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_deck_list, container, false)

        setupViews(rootView)

        return rootView
    }

    override fun setupPresenter() {
        presenter = DecksPresenter(Injection.provideDecksInteractor(context))
    }

    override fun showDeck(deck: Deck) {
        recyclerViewAdapter.addItem(deck)
    }

    override fun removeDeck(deck: Deck) {
        recyclerViewAdapter.removeItem(deck)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(STATE_PUBLIC_DECKS, mPublicDecks)
        super.onSaveInstanceState(outState)
    }

    override fun setLoadingIndicator(loading: Boolean) {

    }

    override fun showGenericErrorMessage() {
        RxBus.post(SnackbarRequest(SnackbarBundle(getString(R.string.general_error))))
    }

    companion object {
        const val REQUEST_CODE = 3414
        private val STATE_PUBLIC_DECKS = "com.jamieadkins.gwent.user.decks"

        fun newInstance(userDecks: Boolean): DeckListFragment {
            val fragment = DeckListFragment()
            fragment.mPublicDecks = userDecks
            return fragment
        }
    }
}
