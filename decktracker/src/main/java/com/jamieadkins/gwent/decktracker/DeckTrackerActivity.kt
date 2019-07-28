package com.jamieadkins.gwent.decktracker

import android.os.Bundle
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.card.data.FactionMapper
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import javax.inject.Inject

class DeckTrackerActivity : DaggerAndroidActivity(), DeckTrackerContract.View {

    @Inject lateinit var factionMapper: FactionMapper
    @Inject lateinit var presenter: DeckTrackerContract.Presenter

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
    }

    override fun showDeckAnalysis(analysis: DeckTrackerAnalysis) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPredictions(cardPredictions: CardPredictions) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}