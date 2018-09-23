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

class ApiMapper : Mapper<FirebaseCardResult, Collection<CardEntity>>() {

    override fun map(from: FirebaseCardResult): Collection<CardEntity> {
        return from.values.mapNotNull {
            if (it.isReleased) {
                val variation = it.variations.values.firstOrNull()
                CardEntity(
                    it.ingameId,
                    it.strength,
                    variation?.isCollectible ?: false,
                    it.rarity ?: "",
                    it.type ?: "",
                    it.faction ?: "",
                    it.name ?: mapOf(),
                    it.info ?: mapOf(),
                    it.flavor ?: mapOf(),
                    it.categories ?: listOf(),
                    it.keywords ?: listOf(),
                    it.loyalties ?: listOf(),
                    it.related ?: listOf()
                )
            } else {
                null
            }
        }
    }
}