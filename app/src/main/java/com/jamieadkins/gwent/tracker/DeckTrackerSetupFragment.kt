package com.jamieadkins.gwent.tracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.list.GwentCardItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.card.data.FromFactionMapper
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.decktracker.DeckTrackerActivity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_tracker_setup.*
import javax.inject.Inject

class DeckTrackerSetupFragment : DaggerFragment(), DeckTrackerSetupContract.View {

    @Inject lateinit var presenter: DeckTrackerSetupContract.Presenter
    @Inject lateinit var factionMapper: FromFactionMapper

    private val adapter = GroupAdapter<ViewHolder>()
    private val factionSection = Section().apply {
        update(
            listOf(
                SubHeaderItem(R.string.deck_tracker_oppo_faction),
                FactionPickerItem { presenter.onFactionSelected(it) }
            )
        )
    }
    private val leadersSection = Section().apply {
        setHeader(SubHeaderItem(R.string.deck_tracker_oppo_leader))
        setHideWhenEmpty(true)
    }

    init {
        adapter.add(factionSection)
        adapter.add(leadersSection)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_tracker_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.card_predictor)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> presenter.onLeaderSelected(item.card.id)
            }
        }

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showLeaders(leaders: List<GwentCard>) {
        leadersSection.update(leaders.map { GwentCardItem(it) })
    }

    override fun launchDeckTracker(faction: GwentFaction, leaderId: String) {
        val factionString = factionMapper.map(faction)
        val intent = Intent(requireContext(), DeckTrackerActivity::class.java)
        intent.data = Uri.parse("roach://decktracker?faction=$factionString&leaderId=$leaderId")
        activity?.startActivity(intent)
    }
}
