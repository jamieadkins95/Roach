package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.card.data.model.FirebaseCardResult
import com.jamieadkins.gwent.card.data.model.Type
import com.jamieadkins.gwent.database.entity.CardEntity
import javax.inject.Inject

class ApiMapper @Inject constructor() {

    fun map(from: FirebaseCardResult): Collection<CardEntity> {
        return from.values.mapNotNull {
            if (it.isReleased) {
                val variation = it.variations.values.firstOrNull()
                CardEntity(
                    it.ingameId,
                    it.strength ?: 0,
                    variation?.isCollectible ?: false,
                    it.rarity ?: "",
                    it.type ?: "",
                    it.faction ?: "",
                    it.cardType ?: "",
                    if (it.type == Type.LEADER_ID) it.provisionBoost else it.provision,
                    it.mulligans ?: 0,
                    it.name ?: mapOf(),
                    it.info ?: mapOf(),
                    it.flavor ?: mapOf(),
                    variation?.craft?.get("standard") ?: 0,
                    variation?.mill?.get("standard") ?: 0,
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