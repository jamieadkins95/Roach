package com.jamieadkins.gwent.deck.detail

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.deck.detail.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deck.detail.rename.RenameDeckDialog
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.CardResourceHelper
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_details.*
import javax.inject.Inject

class DeckDetailsFragment : DaggerFragment(), DeckDetailsContract.View {

    lateinit var deckId: String

    @Inject lateinit var presenter: DeckDetailsContract.Presenter

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

        val layoutManager = LinearLayoutManager(cardDatabase.context)
        cardDatabase.layoutManager = layoutManager
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
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showDeck(deck: GwentDeck) {
        (activity as? AppCompatActivity)?.title = deck.name

        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, deck.faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, deck.faction)
        }
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

    companion object {
        const val KEY_ID = "deckId"
    }
}