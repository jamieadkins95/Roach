package com.jamieadkins.gwent.data.deck.mapper

import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import javax.inject.Inject

class DeckMapper @Inject constructor(private val factionMapper: FactionMapper) : Mapper<DeckEntity, GwentDeck>() {

    override fun map(from: DeckEntity): GwentDeck {
        return GwentDeck(from.id.toString(),
                         from.name,
                         factionMapper.map(from.factionId),
                         from.leaderId)
    }
}