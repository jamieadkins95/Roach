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

        val variation = cardDetails.variations.values.firstOrNull()
        card.collectible = variation?.isCollectible ?: false
        card.cardArt = CardArt().apply {
            original = variation?.art?.original
            high = variation?.art?.high
            medium = variation?.art?.medium
            low = variation?.art?.low
            thumbnail = variation?.art?.thumbnail
            artist = variation?.art?.artist
        }

        cardDetails.loyalties?.forEach { id ->
            loyaltyIdToLoyalty(id)?.let {
                card.loyalties.add(it)
            }
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
            Faction.NEUTRAL_ID -> GwentFaction.NEUTRAL
            else -> null
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

    fun loyaltyIdToLoyalty(loyalty: String): Loyalty? {
        return when (loyalty) {
            com.jamieadkins.gwent.data.card.Loyalty.LOYAL_ID -> Loyalty.LOYAL
            com.jamieadkins.gwent.data.card.Loyalty.DISLOYAL_ID -> Loyalty.DISLOYAL
            else -> null
        }
    }
}