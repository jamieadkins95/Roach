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

class GwentCardArtMapper : Mapper<ArtEntity, GwentCardArt>() {

    override fun map(from: ArtEntity): GwentCardArt {
        return GwentCardArt(
            from.original,
            from.high,
            from.medium,
            from.low,
            from.thumbnail
        )
    }
}