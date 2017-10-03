package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity

/**
 * UI fragment that shows a list of the users decks.
 */

class DeckListFragment : BaseFragment<DeckListContract.View>(), DeckListContract.View {

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
        presenter = DeckListPresenter(Injection.provideDecksInteractor(context))
    }

    override fun showDeck(deck: Deck) {
        recyclerViewAdapter.addItem(deck)
    }

    override fun showDeckDetails(deckId: String, factionId: String) {
        val intent = Intent(activity, UserDeckDetailActivity::class.java)
        intent.putExtra(UserDeckDetailActivity.EXTRA_DECK_ID, deckId)
        intent.putExtra(UserDeckDetailActivity.EXTRA_FACTION_ID, factionId)
        intent.putExtra(UserDeckDetailActivity.EXTRA_IS_PUBLIC_DECK, false)
        context?.startActivity(intent)
    }

    override fun removeDeck(deck: Deck) {
        recyclerViewAdapter.removeItem(deck)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(STATE_PUBLIC_DECKS, mPublicDecks)
        super.onSaveInstanceState(outState)
    }

    override fun setLoadingIndicator(loading: Boolean) {
        if (!loading) {
            refreshLayout.isEnabled = false
        }
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
