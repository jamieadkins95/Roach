package com.jamieadkins.gwent.deck.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.GwentDeckClickEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.deck.create.CreateDeckDialog
import com.jamieadkins.gwent.deck.detail.DeckDetailsActivity
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_list.*
import javax.inject.Inject

class DeckListFragment : DaggerFragment(), DeckListContract.View {

    @Inject lateinit var presenter: DeckListContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()

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

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is DeckListItem -> RxBus.post(GwentDeckClickEvent(item.deck.id))
            }
        }

        btnCreate.setOnClickListener {
            val dialog = CreateDeckDialog()
            dialog.show(activity?.supportFragmentManager, dialog.tag)
        }

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showDecks(decks: List<GwentDeck>) {
       adapter.update(decks.map { DeckListItem(it) })
    }

    override fun showDeckDetails(deckId: String) {
        startActivity(DeckDetailsActivity.getIntent(requireContext(), deckId))
    }

    override fun showLoadingIndicator(loading: Boolean) { loadingIndicator.isRefreshing = loading }
}
