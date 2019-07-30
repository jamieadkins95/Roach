package com.jamieadkins.gwent.decktracker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.base.CardResourceHelper
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.FeatureNavigator
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.base.items.H2HeaderItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.card.data.FactionMapper
import com.jamieadkins.gwent.decktracker.cardpicker.CardPickerDialog
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.tracker.predictions.CardPrediction
import com.jamieadkins.gwent.domain.tracker.predictions.SimilarDeck
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_deck_tracker.*
import javax.inject.Inject
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.TouchCallback

class DeckTrackerActivity : DaggerAndroidActivity(), DeckTrackerContract.View {

    @Inject lateinit var factionMapper: FactionMapper
    @Inject lateinit var presenter: DeckTrackerContract.Presenter
    private val featureNavigator = FeatureNavigator(this)

    private val adapter = GroupAdapter<ViewHolder>()
    private val headerSection = Section().apply {
        update(listOf(
            BetaNoticeItem { presenter.onFeedbackClicked() },
            H2HeaderItem(R.string.cards_played_title, R.string.cards_played_subtitle)
        ))
    }
    private val cardsPlayedSection = Section().apply {
        setHeader(headerSection)
        setPlaceholder(CardsPlayedPlaceholderItem())
    }
    private val remainingSection = Section().apply {
        setHeader(SubHeaderItem(R.string.remaining_in_opponents_deck))
        setHideWhenEmpty(true)
    }
    private val predictionsSection = Section().apply {
        setHeader(H2HeaderItem(R.string.card_predictions_title, R.string.card_predictions_subtitle))
        setHideWhenEmpty(true)
    }
    private val similarDecksSection = Section().apply {
        setHeader(H2HeaderItem(R.string.similar_decks_title, R.string.similar_decks_subtitle))
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
        toolbar.setTitleTextAppearance(this, R.style.GwentTextAppearance)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val dividerItemDecoration = VerticalSpaceItemDecoration(convertDpToPixel(8f).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)

        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is SimpleGwentCardItem -> presenter.onOpponentCardPlayed(item.card.id)
                is PredictedCardItem -> presenter.onOpponentCardPlayed(item.card.id)
                is SimilarDeckItem -> presenter.onSimilarDeckClicked(item.deck)
            }
        }

        adapter.setOnItemLongClickListener { item, _ ->
            when (item) {
                is SimpleGwentCardItem -> { presenter.onCardClicked(item.card.id); true }
                is PredictedCardItem -> { presenter.onCardClicked(item.card.id); true }
                else -> false
            }
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deck_tracker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_feedback -> { presenter.onFeedbackClicked(); true }
            R.id.action_gwent_deck_library -> { openGwentDeckLibrary(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showCardsPlayed(cards: List<GwentCard>) {
        cardsPlayedSection.update(
            cards.mapIndexed { index: Int, card: GwentCard ->
                val id = card.id.toLongOrNull() ?: card.hashCode().toLong()
                val unique = id.times(10 * (index + 1))
                SimpleGwentCardItem(card, unique)
            }
        )
    }

    override fun showDeckAnalysis(opponentProvisionsRemaining: Int, opponentAverageProvisionsRemaining: Float) {
        remainingSection.update(listOf(DeckAnalysisItem(opponentProvisionsRemaining, opponentAverageProvisionsRemaining)))
    }

    override fun showPredictions(cardPredictions: List<CardPrediction>) {
        if (cardPredictions.isNotEmpty()) {
            predictionsSection.update(cardPredictions.map { PredictedCardItem(it.card, it.percentage) })
        } else {
            predictionsSection.update(listOf(NoPredictionsItem()))
        }
    }

    override fun showSimilarDecks(similar: List<SimilarDeck>) {
        similarDecksSection.update(similar.map(::SimilarDeckItem))
    }

    override fun showFaction(faction: GwentFaction) {
        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, faction)
        }
    }

    override fun showLeader(card: GwentCard) { toolbar.title = card.name }

    override fun openCardDetails(cardId: String) = featureNavigator.openCardDetails(cardId)

    override fun openSimilarDeck(deckUrl: String) = openUrl(deckUrl)

    override fun openGwentDeckLibrary() {
        openUrl("https://www.playgwent.com/en/decks")
    }

    override fun openFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:jamieadkins95+gwent@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Roach Deck Tracker")
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setShowTitle(true)
            .build()
            .launchUrl(this, Uri.parse(url))
    }

    private val touchCallback = object : TouchCallback() {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = adapter.getItem(viewHolder.adapterPosition)
            cardsPlayedSection.remove(item)
            (item as? SimpleGwentCardItem)?.let { presenter.onOpponentCardDeleted(item.card.id) }
        }
    }
}