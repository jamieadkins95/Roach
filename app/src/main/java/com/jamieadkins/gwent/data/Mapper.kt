package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.data.card.CardDetails
import com.jamieadkins.gwent.data.card.Faction
import com.jamieadkins.gwent.data.card.Type
import com.jamieadkins.gwent.model.*

object Mapper {

    fun cardDetailsToGwentCard(cardDetails: CardDetails): GwentCard {
        val card = GwentCard()
        card.id = cardDetails.ingameId
        card.strength = cardDetails.strength
        card.name = cardDetails.name
        card.info = cardDetails.info
        card.flavor = cardDetails.flavor
        card.faction = factionIdToFaction(cardDetails.faction)
        card.rarity = rarityIdToRarity(cardDetails.rarity)
        card.colour = typeToColour(cardDetails.type)

        card.cardArt = CardArt().apply {
            val variation = cardDetails.variations.values.firstOrNull()
            original = variation?.art?.original
            high = variation?.art?.high
            medium = variation?.art?.medium
            low = variation?.art?.low
            thumbnail = variation?.art?.thumbnail
            artist = variation?.art?.artist
        }

        return card
    }

    fun cardDetailsListToGwentCardList(list: List<CardDetails>): List<GwentCard> {
        val cards = mutableListOf<GwentCard>()
        list.forEach {
            if (it.isReleased) {
                cards.add(Mapper.cardDetailsToGwentCard(it))
            }
        }
        return cards
    }

    fun factionIdToFaction(factionId: String): GwentFaction? {
        return when (factionId) {
            Faction.MONSTERS_ID -> GwentFaction.MONSTER
            Faction.NORTHERN_REALMS_ID -> GwentFaction.NORTHERN_REALMS
            Faction.SCOIATAEL_ID -> GwentFaction.SCOIATAEL
            Faction.SKELLIGE_ID -> GwentFaction.SKELLIGE
            Faction.NILFGAARD_ID -> GwentFaction.NILFGAARD
            else -> null
        }
    }

    fun typeToColour(type: String): CardColour? {
        return when (type) {
            Type.BRONZE_ID -> CardColour.BRONZE
            Type.SILVER_ID -> CardColour.SILVER
            Type.GOLD_ID -> CardColour.GOLD
            Type.LEADER_ID -> CardColour.LEADER
            else -> null
        }
    }

    fun rarityIdToRarity(rarity: String): Rarity? {
        return when (rarity) {
            com.jamieadkins.gwent.data.card.Rarity.COMMON_ID -> Rarity.COMMON
            com.jamieadkins.gwent.data.card.Rarity.RARE_ID -> Rarity.RARE
            com.jamieadkins.gwent.data.card.Rarity.EPIC_ID -> Rarity.EPIC
            com.jamieadkins.gwent.data.card.Rarity.LEGENDARY_ID -> Rarity.LEGENDARY
            else -> null
        }
    }
}