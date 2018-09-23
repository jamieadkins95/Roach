package com.jamieadkins.gwent.data.card.mapper

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

object GwentCardMapper {
    private const val DEFAULT_LOCALE = "en-US"

    fun gwentCardListFromCardEntityList(entityList: Collection<CardWithArtEntity>): Collection<GwentCard> {
        val cardList = mutableListOf<GwentCard>()
        entityList.forEach {
            cardList.add(cardEntityToGwentCard(it))
        }
        return cardList
    }

    fun cardEntityToGwentCard(toMap: CardWithArtEntity, locale: String = DEFAULT_LOCALE): GwentCard {
        val cardEntity = toMap.card
        return GwentCard(cardEntity.id,
                         cardEntity.name[locale] ?: "",
                         cardEntity.tooltip[locale] ?: "",
                         cardEntity.flavor[locale] ?: "",
                         emptyList(),
                         factionIdToFaction(cardEntity.faction),
                         cardEntity.strength,
                         typeToColour(cardEntity.color),
                         rarityIdToRarity(cardEntity.rarity),
                         cardEntity.collectible,
                         0,
                         0,
                         cardEntity.related,
                         artEntityToCardArt(toMap.art.first()))
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
                        it.keywords ?: listOf(),
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
        art.low = artEntity.low
        art.thumbnail = artEntity.thumbnail
        return art
    }

    fun factionIdToFaction(factionId: String): GwentFaction {
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

    fun factionToFactionId(faction: GwentFaction): String {
        return when (faction) {
            GwentFaction.MONSTER -> Faction.MONSTERS_ID
            GwentFaction.NORTHERN_REALMS -> Faction.NORTHERN_REALMS_ID
            GwentFaction.SCOIATAEL -> Faction.SCOIATAEL_ID
            GwentFaction.SKELLIGE -> Faction.SKELLIGE_ID
            GwentFaction.NILFGAARD -> Faction.NILFGAARD_ID
            GwentFaction.NEUTRAL -> Faction.NEUTRAL_ID
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
            Rarity.COMMON_ID -> GwentCardRarity.COMMON
            Rarity.RARE_ID -> GwentCardRarity.RARE
            Rarity.EPIC_ID -> GwentCardRarity.EPIC
            Rarity.LEGENDARY_ID -> GwentCardRarity.LEGENDARY
            else -> throw Exception("Rarity not found")
        }
    }
}