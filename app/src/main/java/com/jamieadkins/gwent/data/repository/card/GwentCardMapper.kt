package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.core.*
import com.jamieadkins.gwent.data.card.Faction
import com.jamieadkins.gwent.data.card.Type
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity

object GwentCardMapper {
    private const val DEFAULT_LOCALE = "en-US"

    fun gwentCardListFromCardEntityList(entityList: Collection<CardEntity>): Collection<GwentCard> {
        val cardList = mutableListOf<GwentCard>()
        entityList.forEach {
            cardList.add(cardEntityToGwentCard(it))
        }
        return cardList
    }

    fun cardEntityToGwentCard(cardEntity: CardEntity, locale: String = DEFAULT_LOCALE): GwentCard {
        val card = GwentCard()
        card.id = cardEntity.id
        card.name = cardEntity.name[locale]
        card.tooltip = cardEntity.tooltip[locale]
        card.flavor = cardEntity.flavor[locale]
        card.strength = cardEntity.strength
        card.collectible = cardEntity.collectible

        card.faction = factionIdToFaction(cardEntity.faction)
        card.colour = typeToColour(cardEntity.color)
        card.rarity = rarityIdToRarity(cardEntity.rarity)
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

    private fun artEntityToCardArt(artEntity: ArtEntity): GwentCardArt {
        val art = GwentCardArt()
        art.original = artEntity.original
        art.high = artEntity.high
        art.medium = artEntity.medium
        art.low= artEntity.low
        art.thumbnail = artEntity.thumbnail
        return art
    }

    private fun factionIdToFaction(factionId: String): GwentFaction {
        return when (factionId) {
            Faction.MONSTERS_ID -> GwentFaction.MONSTER
            Faction.NORTHERN_REALMS_ID -> GwentFaction.NORTHERN_REALMS
            Faction.SCOIATAEL_ID -> GwentFaction.SCOIATAEL
            Faction.SKELLIGE_ID -> GwentFaction.SKELLIGE
            Faction.NILFGAARD_ID -> GwentFaction.NILFGAARD
            Faction.NEUTRAL_ID -> GwentFaction.NEUTRAL
            else -> throw Exception("Faction not found")
        }
    }

    private fun typeToColour(type: String): GwentCardColour {
        return when (type) {
            Type.BRONZE_ID -> GwentCardColour.BRONZE
            Type.SILVER_ID -> GwentCardColour.SILVER
            Type.GOLD_ID -> GwentCardColour.GOLD
            Type.LEADER_ID -> GwentCardColour.LEADER
            else -> throw Exception("Colour not found")
        }
    }

    private fun rarityIdToRarity(rarity: String): GwentCardRarity {
        return when (rarity) {
            com.jamieadkins.gwent.data.card.Rarity.COMMON_ID -> GwentCardRarity.COMMON
            com.jamieadkins.gwent.data.card.Rarity.RARE_ID -> GwentCardRarity.RARE
            com.jamieadkins.gwent.data.card.Rarity.EPIC_ID -> GwentCardRarity.EPIC
            com.jamieadkins.gwent.data.card.Rarity.LEGENDARY_ID -> GwentCardRarity.LEGENDARY
            else -> throw Exception("Rarity not found")
        }
    }
}