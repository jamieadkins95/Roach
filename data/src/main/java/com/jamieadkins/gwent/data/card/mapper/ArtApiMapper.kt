package com.jamieadkins.gwent.data.card.mapper

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.card.model.Faction
import com.jamieadkins.gwent.data.card.model.FirebaseCardResult
import com.jamieadkins.gwent.data.card.model.Rarity
import com.jamieadkins.gwent.data.card.model.Type
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardArt
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity

class ArtApiMapper : Mapper<FirebaseCardResult, Collection<ArtEntity>>() {

    override fun map(from: FirebaseCardResult): Collection<ArtEntity> {
        return from.values.mapNotNull { card ->
            if (card.isReleased) {
                card.variations.values.firstOrNull()?.let { variation ->
                    ArtEntity(
                        variation.variationId,
                        card.ingameId,
                        variation.art.original,
                        variation.art.high,
                        variation.art.medium,
                        variation.art.low,
                        variation.art.thumbnail
                    )
                }
            } else {
                null
            }
        }
    }
}