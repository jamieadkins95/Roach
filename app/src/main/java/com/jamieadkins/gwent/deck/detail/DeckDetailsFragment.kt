package com.jamieadkins.gwent.deck.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.detail.CardDetailsActivity
import com.jamieadkins.gwent.card.detail.CardDetailsFragment
import com.jamieadkins.gwent.card.list.GwentCardItem
import com.jamieadkins.gwent.base.items.HeaderItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.card.list.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.deck.DeckBuilderEvent
import com.jamieadkins.gwent.deck.DeckBuilderEvents
import com.jamieadkins.gwent.deck.detail.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deck.detail.rename.RenameDeckDialog
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.main.CardResourceHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_details.*
import javax.inject.Inject

class DeckDetailsFragment : DaggerFragment(), DeckDetailsContract.View {

    lateinit var deckId: String

    @Inject lateinit var presenter: DeckDetailsContract.Presenter

    private val cardDatabaseAdapter = GroupAdapter<ViewHolder>()
    private val deckAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        deckId = arguments?.getString(KEY_ID) ?: throw Exception("Deck id not found.")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
        }
        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        val cardDatabaseLayoutManager = LinearLayoutManager(cardDatabase.context)
        cardDatabase.layoutManager = cardDatabaseLayoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        cardDatabase.addItemDecoration(dividerItemDecoration)
        cardDatabase.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (cardDatabaseLayoutManager.findLastVisibleItemPosition() -
                    cardDatabaseLayoutManager.findFirstVisibleItemPosition() + 1 < cardDatabaseAdapter.itemCount) {
                    buttonReturnToTop.visibility = if (dy < 0) View.VISIBLE else View.GONE
                }
                if (cardDatabaseLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    buttonReturnToTop.visibility = View.GONE
                }
            }
        })

        buttonReturnToTop.setOnClickListener {
            cardDatabase.scrollToPosition(0)
        }
        cardDatabase.adapter = cardDatabaseAdapter
        cardDatabaseAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> DeckBuilderEvents.post(DeckBuilderEvent.CardDatabaseClick(item.card.id))
            }
        }
        cardDatabaseAdapter.setOnItemLongClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> DeckBuilderEvents.post(DeckBuilderEvent.CardDatabaseLongClick(item.card.id))
            }
            true
        }


        deckList.layoutManager = LinearLayoutManager(deckList.context)
        deckList.addItemDecoration(dividerItemDecoration)
        deckList.adapter = deckAdapter
        deckAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is DeckLeaderItem -> DeckBuilderEvents.post(DeckBuilderEvent.LeaderClick(item.leader.id))
                is DeckCardItem -> DeckBuilderEvents.post(DeckBuilderEvent.DeckClick(item.card.id))
            }
        }
        deckAdapter.setOnItemLongClickListener { item, _ ->
            when (item) {
                is DeckLeaderItem -> DeckBuilderEvents.post(DeckBuilderEvent.LeaderLongClick(item.leader.id))
                is DeckCardItem -> DeckBuilderEvents.post(DeckBuilderEvent.DeckLongClick(item.card.id))
            }
            true
        }

        refreshLayout.setColorSchemeResources(R.color.gwentAccent)

        presenter.setDeckId(deckId)
        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search, menu)
        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.search(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener {
            presenter.clearSearch()
            false
        }

        inflater?.inflate(R.menu.deck_builder, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_rename -> {
                presenter.onRenameClicked()
                true
            }
            R.id.action_change_leader -> {
                presenter.onChangeLeaderClicked()
                true
            }
            R.id.action_delete -> {
                presenter.onDeleteClicked()
                true
            }
            R.id.action_filter -> {
                val dialog = FilterBottomSheetDialogFragment.getInstance(deckId)
                dialog.show(activity?.supportFragmentManager, dialog.javaClass.simpleName)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showCardDatabase(cards: List<GwentCard>, searchQuery: String) {
        // Diffing here is too expensive.
        cardDatabaseAdapter.clear()

        val searchResults: List<Item> = if (searchQuery.isNotEmpty()) {
            listOf(
                HeaderItem(
                    R.string.search_results, resources.getString(R.string.results_found, cards.size, searchQuery)
                )
            )
        } else {
            emptyList()
        }
        cardDatabaseAdapter.update(searchResults + cards.map { GwentCardItem(it) } + listOf(SpaceItem()))
    }

    override fun showDeck(deck: GwentDeck) {
        (activity as? AppCompatActivity)?.title = deck.name

        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, deck.faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, deck.faction)
        }

        val cards = deck.cards.values.sortedWith(compareByDescending<GwentCard> { it.provisions }.thenBy { it.name })
        deckAdapter.update(
            listOf(
                DeckHeaderItem(deck.id, deck.name, deck.totalCardCount, deck.unitCount, deck.provisionCost, deck.maxProvisionCost),
                SubHeaderItem(R.string.leader),
                DeckLeaderItem(deck.leader),
                SubHeaderItem(R.string.cards)
            ) +
            cards.map { DeckCardItem(it, deck.cardCounts[it.id] ?: 0) }
        )
    }

    override fun showLeaderPicker() {
        val dialog = LeaderPickerDialog.newInstance(deckId)
        dialog.show(activity?.supportFragmentManager, dialog.tag)
    }

    override fun showRenameDeckMenu() {
        val dialog = RenameDeckDialog.newInstance(deckId)
        dialog.show(activity?.supportFragmentManager, dialog.tag)
    }

    override fun showLoadingIndicator() {
        refreshLayout.isEnabled = true
        refreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        refreshLayout.isEnabled = false
        refreshLayout.isRefreshing = false
    }

    override fun close() {
        activity?.finish()
    }

    override fun showCardDetails(cardId: String) {
        val intent = Intent(requireContext(), CardDetailsActivity::class.java)
        intent.putExtra(CardDetailsFragment.KEY_ID, cardId)
        activity?.startActivity(intent)
    }

    override fun showMaximumCardCountReached() {
        context?.let {
            Toast.makeText(it, R.string.maximum_card_count_reached, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val KEY_ID = "deckId"
    }
}