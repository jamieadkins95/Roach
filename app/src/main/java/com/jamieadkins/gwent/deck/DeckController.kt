package com.jamieadkins.gwent.deck

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.R

class DeckController : TypedEpoxyController<List<String>>() {
    @AutoModel
    lateinit var leaderDivider: DividerViewModel_

    @AutoModel
    lateinit var goldDivider: DividerViewModel_

    @AutoModel
    lateinit var silverDivider: DividerViewModel_

    @AutoModel
    lateinit var bronzeDivider: DividerViewModel_

    override fun buildModels(headers: List<String>) {
        leaderDivider
                .title(R.string.leader)
                .color(R.color.gold)
                .addTo(this)

        goldDivider
                .title(R.string.gold)
                .color(R.color.gold)
                .addTo(this)

        silverDivider
                .title(R.string.silver)
                .color(R.color.silver)
                .addTo(this)

        bronzeDivider
                .title(R.string.bronze)
                .color(R.color.bronze)
                .addTo(this)
    }
}