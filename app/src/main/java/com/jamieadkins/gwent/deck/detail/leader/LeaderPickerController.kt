package com.jamieadkins.gwent.deck.detail.leader

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.LeaderPickerEvent
import com.jamieadkins.gwent.card.list.GwentCardViewModel_
import com.jamieadkins.gwent.card.list.SubHeaderViewModel_
import com.jamieadkins.gwent.domain.card.model.GwentCard

class LeaderPickerController(val resources: Resources) : TypedEpoxyController<List<GwentCard>>() {

    @AutoModel lateinit var subheaderView: SubHeaderViewModel_

    override fun buildModels(data: List<GwentCard>) {

        subheaderView
                .title(resources.getString(R.string.change_leader))
                .addTo(this)

        data.forEach { card ->
            val model = GwentCardViewModel_()
                    .id(card.id)
                    .cardName(card.name)
                    .cardTooltip(card.tooltip)
                    .cardCategories(card.categories)
                    .cardProvisions(card.provisions)
                    .cardImage(card.cardArt.medium)
                    .cardFaction(card.faction)
                    .cardRarity(card.rarity)
                    .clickListener { _ -> RxBus.post(LeaderPickerEvent(card.id)) }

            model.addTo(this)
        }
    }
}