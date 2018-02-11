package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.database.entity.CardEntity
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
}