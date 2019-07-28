package com.jamieadkins.gwent.decktracker

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.card.data.FactionMapper
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_deck_tracker.*
import javax.inject.Inject

class DeckTrackerActivity : DaggerAndroidActivity(), DeckTrackerContract.View {

    @Inject lateinit var factionMapper: FactionMapper
    @Inject lateinit var presenter: DeckTrackerContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()
    private val analysisSection = Section()
    private val predictionsSection = Section()
    private val similarDecksSection = Section()

    init {
        adapter.add(analysisSection)
        adapter.add(predictionsSection)
        adapter.add(similarDecksSection)
    }

    override fun onInject() {
        DaggerDeckTrackerComponent.builder()
            .core(coreComponent)
            .activity(this)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_tracker)
        val url = intent?.data
        val faction = url?.getQueryParameter("faction")?.let { factionMapper.map(it) }
        val leaderId = url?.getQueryParameter("leaderId")

        if (faction != null && leaderId != null) {
            presenter.init(faction, leaderId)
        } else {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun showDeckAnalysis(analysis: DeckTrackerAnalysis) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPredictions(cardPredictions: CardPredictions) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}