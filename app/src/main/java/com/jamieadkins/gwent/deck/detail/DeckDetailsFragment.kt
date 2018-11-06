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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.detail.CardDetailsActivity
import com.jamieadkins.gwent.card.detail.CardDetailsFragment
import com.jamieadkins.gwent.card.list.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.deck.detail.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deck.detail.rename.RenameDeckDialog
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.main.CardResourceHelper
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_details.*
import javax.inject.Inject

class DeckDetailsFragment : DaggerFragment(), DeckDetailsContract.View {

    lateinit var deckId: String

    @Inject lateinit var presenter: DeckDetailsContract.Presenter

    private lateinit var cardDatabaseController: DeckCardDatabaseController
    private lateinit var deckController: DeckController

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

        cardDatabase.layoutManager = LinearLayoutManager(cardDatabase.context)
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        cardDatabase.addItemDecoration(dividerItemDecoration)
        cardDatabaseController = DeckCardDatabaseController(resources)
        cardDatabase.adapter = cardDatabaseController.adapter

        deckController = DeckController(resources)
        deckList.layoutManager = LinearLayoutManager(deckList.context)
        deckList.addItemDecoration(dividerItemDecoration)
        deckList.adapter = deckController.adapter

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
        cardDatabaseController.setData(cards, searchQuery)
    }

    override fun showDeck(deck: GwentDeck) {
        (activity as? AppCompatActivity)?.title = deck.name

        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, deck.faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, deck.faction)
        }

        deckController.setData(deck)
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

    companion object {
        const val KEY_ID = "deckId"
    }
}