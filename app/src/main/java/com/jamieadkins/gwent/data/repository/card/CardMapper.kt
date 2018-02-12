package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.model.CardArt
import com.jamieadkins.gwent.model.GwentCard

object CardMapper {
    fun gwentCardListFromCardEntityList(entityList: Collection<CardEntity>): Collection<GwentCard> {
        val cardList = mutableListOf<GwentCard>()
        entityList.forEach {
            cardList.add(cardEntityToGwentCard(it))
        }
        return cardList
    }

    fun cardEntityToGwentCard(cardEntity: CardEntity): GwentCard {
        val card = GwentCard()
        card.id = cardEntity.id
        card.name = cardEntity.name
        card.info = cardEntity.tooltip
        card.flavor = cardEntity.flavor
        card.strength = cardEntity.strength
        card.collectible = cardEntity.collectible

        card.faction = Mapper.factionIdToFaction(cardEntity.faction)
        card.colour = Mapper.typeToColour(cardEntity.color)
        card.rarity = Mapper.rarityIdToRarity(cardEntity.rarity)
        card.cardArt = cardEntity.art?.firstOrNull()?.let { artEntityToCardArt(it) }
        return card
    }

    fun cardEntityListFromApiResult(result: FirebaseCardResult): Collection<CardEntity> {
        val cardList = mutableListOf<CardEntity>()
        result.values.forEach {
            if (it.isReleased) {
                val variation = it.variations.values.firstOrNull()
                cardList.add(
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
                                it.loyalties ?: listOf(),
                                it.related ?: listOf()
                        )
                )
            }
        }
        return cardList
    }

    fun artEntityListFromApiResult(result: FirebaseCardResult): Collection<ArtEntity> {
        val artList = mutableListOf<ArtEntity>()
        result.values.forEach { card ->
            if (card.isReleased) {
                val variation = card.variations.values.firstOrNull()
                variation?.let { variation ->
                    artList.add(
                            ArtEntity(
                                    variation.variationId,
                                    card.ingameId,
                                    variation.art.original,
                                    variation.art.high,
                                    variation.art.medium,
                                    variation.art.low,
                                    variation.art.thumbnail
                            )
                    )
                }
            }
        }
        return artList
    }

    private fun artEntityToCardArt(artEntity: ArtEntity): CardArt {
        val art = CardArt()
        art.original = artEntity.original
        art.high = artEntity.high
        art.medium = artEntity.medium
        art.low= artEntity.low
        art.thumbnail = artEntity.thumbnail
        return art
    }
}