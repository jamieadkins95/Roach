package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity
import com.jamieadkins.gwent.model.deck.GwentDeckSummary

/**
 * UI fragment that shows a list of the users decks.
 */

class DeckListFragment : BaseFragment<DeckListContract.View>(), DeckListContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.my_decks)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_deck_list, container, false)
        setupViews(rootView)
        return rootView
    }

    override fun setupPresenter() {
        presenter = DeckListPresenter(
                Injection.provideSchedulerProvider(),
                Injection.provideDeckRepository())
    }

    override fun showDecks(decks: MutableCollection<GwentDeckSummary>) {
        recyclerViewAdapter.items = decks.toList()
    }

    override fun showDeckDetails(deckId: String) {
        val intent = Intent(activity, UserDeckDetailActivity::class.java)
        intent.putExtra(UserDeckDetailActivity.EXTRA_DECK_ID, deckId)
        context?.startActivity(intent)
    }

    override fun setLoadingIndicator(loading: Boolean) {
        if (!loading) {
            refreshLayout.isEnabled = false
        }
    }

    override fun showEmptyView() {
        RxBus.post(SnackbarRequest(SnackbarBundle(getString(R.string.no_results), Snackbar.LENGTH_LONG)))
    }

    override fun showGenericErrorMessage() {
        RxBus.post(SnackbarRequest(SnackbarBundle(getString(R.string.general_error))))
    }
}
