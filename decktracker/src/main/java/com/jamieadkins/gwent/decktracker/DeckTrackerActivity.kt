package com.jamieadkins.gwent.decktracker

import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.base.CardResourceHelper
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.base.items.H2HeaderItem
import com.jamieadkins.gwent.base.items.HeaderItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.card.data.FactionMapper
import com.jamieadkins.gwent.decktracker.cardpicker.CardPickerDialog
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
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
    private val cardsPlayedSection = Section().apply {
        setHeader(H2HeaderItem(R.string.cards_played_title, R.string.cards_played_subtitle))
        setPlaceholder(CardsPlayedPlaceholderItem())
    }
    private val remainingSection = Section().apply {
        setHeader(SubHeaderItem(R.string.remaining_in_opponents_deck))
        setHideWhenEmpty(true)
    }
    private val predictionsSection = Section().apply {
        setHeader(HeaderItem(R.string.card_predictions_title, secondaryTextRes = R.string.card_predictions_subtitle))
        setHideWhenEmpty(true)
    }
    private val similarDecksSection = Section().apply {
        setHeader(SubHeaderItem(R.string.similar_decks))
        setHideWhenEmpty(true)
    }

    init {
        adapter.add(cardsPlayedSection)
        adapter.add(remainingSection)
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
        val factionId = url?.getQueryParameter("faction")
        val faction = factionId?.let(factionMapper::map)
        val leaderId = url?.getQueryParameter("leaderId")

        if (faction != null && leaderId != null) {
            presenter.init(faction, leaderId)
        } else {
            finish()
        }

        setSupportActionBar(toolbar)
        title = getString(R.string.deck_tracker)
        toolbar.setTitleTextAppearance(this, R.style.GwentTextAppearance)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val dividerItemDecoration = VerticalSpaceItemDecoration(convertDpToPixel(8f).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)

        btnAddCard.setOnClickListener {
            factionId?.let {
                val dialog = CardPickerDialog.withFaction(factionId)
                dialog.show(supportFragmentManager, dialog.tag)
            }
        }

        presenter.onAttach()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun showCardsPlayed(cards: List<GwentCard>) {
        cardsPlayedSection.update(cards.map(::SimpleGwentCardItem))
    }

    override fun showDeckAnalysis(opponentProvisionsRemaining: Int, opponentAverageProvisionsRemaining: Float) {
        remainingSection.update(listOf(DeckAnalysisItem(opponentProvisionsRemaining, opponentAverageProvisionsRemaining)))
    }

    override fun showPredictions(cardPredictions: CardPredictions) {
        predictionsSection.update(cardPredictions.cards.map { PredictedCardItem(it.card, it.percentage) })
    }

    override fun showFaction(faction: GwentFaction) {
        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, faction)
        }
    }
}