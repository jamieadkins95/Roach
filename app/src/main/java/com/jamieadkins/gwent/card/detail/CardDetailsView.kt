package com.jamieadkins.gwent.card.detail

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.list.GwentCardItem
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.main.GwentStringHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder as GroupieViewHolder

class CardDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val cardAdapter = GroupAdapter<GroupieViewHolder>()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = cardAdapter
        addItemDecoration( VerticalSpaceItemDecoration(context.convertDpToPixel(8f).toInt()))

        cardAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> RxBus.post(GwentCardClickEvent(item.card.id))
            }
        }
    }

    fun showCardDetails(card: GwentCard, relatedCards: List<GwentCard>) {
        val items = mutableListOf<Item>()
        if (card.tooltip.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.tooltip))
            items.add(ElevatedTextItem(card.tooltip))
        }
        if (card.keywords.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.keywords))
            items.addAll(card.keywords.map { ElevatedTextItem(it.description) })
        }
        if (card.flavor.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.flavor))
            items.add(ElevatedTextItem(card.flavor, Typeface.defaultFromStyle(Typeface.ITALIC)))
        }
        if (card.secondaryFaction != null) {
            val factionName = GwentStringHelper.getFactionString(context, card.secondaryFaction)
            factionName?.let {
                items.add(SubHeaderItem(R.string.secondary_faction))
                items.add(ElevatedTextItem(it))
            }
        }
        if (card.categories.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.categories))
            items.add(ElevatedTextItem(card.categories.joinToString()))
        }
        items.add(SubHeaderItem(R.string.type))
        items.add(ElevatedTextItem(GwentStringHelper.getTypeString(resources, card.type)))

        if (card.colour != GwentCardColour.LEADER && card.provisions > 0) {
            items.add(SubHeaderItem(R.string.provisions))
            items.add(ElevatedTextItem(card.provisions.toString()))
        }

        if (card.strength > 0) {
            items.add(SubHeaderItem(R.string.strength))
            items.add(ElevatedTextItem(card.strength.toString()))
        }

        if (card.colour == GwentCardColour.LEADER) {
            items.add(SubHeaderItem(R.string.extra_provisions))
            items.add(ElevatedTextItem(card.extraProvisions.toString()))
        }

        if (card.collectible) {
            items.add(SubHeaderItem(R.string.craft))
            items.add(CraftCostItem(card.craftCost))
            items.add(SubHeaderItem(R.string.mill))
            items.add(CraftCostItem(card.millValue))
        }

        if (card.relatedCards.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.related))
            items.addAll(relatedCards.map { GwentCardItem(it) })
        }

        cardAdapter.update(items)
    }
}