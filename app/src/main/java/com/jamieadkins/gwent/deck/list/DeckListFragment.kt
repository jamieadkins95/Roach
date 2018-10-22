package com.jamieadkins.gwent.deck.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_collection_placeholder.*
import kotlinx.android.synthetic.main.fragment_deck_list.*
import timber.log.Timber
import javax.inject.Inject

class DeckListFragment : DaggerFragment(), DeckListContract.View {

    @Inject lateinit var presenter: DeckListContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.deck_builder)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        loadingIndicator.setColorSchemeResources(R.color.gwentAccent)
        loadingIndicator.isEnabled = false
        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showDecks(decks: List<GwentDeck>) {
        Timber.e("Decks: ${decks.size}")
    }

    override fun showDeckDetails(deckId: String) {
        Timber.e("Decks: $deckId")
    }

    override fun showLoadingIndicator(loading: Boolean) { loadingIndicator.isRefreshing = loading }
}
