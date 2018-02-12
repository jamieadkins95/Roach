package com.jamieadkins.gwent.deck.detail.user

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.deck.detail.BaseDeckDetailFragment
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.GwentCard

/**
 * UI fragment that shows a list of the users decks.
 */

class UserDeckDetailFragment : BaseDeckDetailFragment<UserDeckDetailsContract.View>(), UserDeckDetailsContract.View {
    private var deckDetailsPresenter: UserDeckDetailsContract.Presenter? = null

    override fun setupPresenter() {
        context?.let {
            val newPresenter = UserDeckDetailsPresenter(
                    mDeckId,
                    mFaction,
                    Injection.provideDecksInteractor(it),
                    Injection.provideCardsInteractor(it),
                    Injection.provideSchedulerProvider())
            deckDetailsPresenter = newPresenter
            presenter = newPresenter
        }
    }

    override fun showPotentialLeaders(potentialLeaders: List<GwentCard>) {
        mPotentialLeaders = potentialLeaders
        activity?.invalidateOptionsMenu()
    }

    private var mPotentialLeaders: List<GwentCard>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    public override fun getLayoutId(): Int {
        return R.layout.fragment_user_deck_detail
    }

    override fun onBuildRecyclerView(): GwentRecyclerViewAdapter {
        return GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.DECK)
                .build()
    }

    override fun initialiseCardFilter(): CardFilter {
        val filter = CardFilter()
        filter.colourFilter.put(CardColour.LEADER, false)
        filter.isCollectibleOnly = true
        for (faction in GwentFaction.values()) {
            if (faction != mFaction) {
                filter.factionFilter.put(faction, false)
            }
        }
        filter.factionFilter.put(GwentFaction.NEUTRAL, true)
        filter.setCurrentFilterAsBase()
        return filter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (mPotentialLeaders != null && mPotentialLeaders!!.size >= 3) {

            val key = getString(R.string.pref_locale_key)
            val locale = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(key, getString(R.string.default_locale))

            inflater.inflate(R.menu.deck_builder, menu)
            val subMenu = menu.findItem(R.id.action_change_leader).subMenu
            for (leader in mPotentialLeaders!!) {
                subMenu.add(leader.name[locale])
                        .setOnMenuItemClickListener {
                            deckDetailsPresenter?.changeLeader(leader.id!!)
                            true
                        }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rename -> {
                activity?.let { activity ->
                    val builder = AlertDialog.Builder(activity)
                    val inflater = activity.layoutInflater
                    val view = inflater.inflate(R.layout.dialog_edit_text, null)
                    val input: EditText = view.findViewById<EditText>(R.id.edit_text) as EditText
                    input.setText(mDeck.name)
                    input.setHint(R.string.new_name)
                    builder.setView(view)
                            .setTitle(R.string.rename)
                            .setPositiveButton(R.string.rename) { dialog, which -> deckDetailsPresenter?.renameDeck(input.text.toString()) }
                            .setNegativeButton(android.R.string.cancel, null)
                            .create()
                            .show()
                }

                return true
            }
            R.id.action_delete -> {
                activity?.let { activity ->
                    AlertDialog.Builder(activity)
                            .setMessage(R.string.confirm_delete)
                            .setPositiveButton(R.string.delete) { dialog, which ->
                                deckDetailsPresenter?.deleteDeck()
                                activity.finish()
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                            .create()
                            .show()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(deckId: String, factionId: GwentFaction): UserDeckDetailFragment {
            val fragment = UserDeckDetailFragment()
            fragment.mDeckId = deckId
            fragment.mFaction = factionId
            return fragment
        }
    }
}
