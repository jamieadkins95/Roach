package com.jamieadkins.gwent.domain.card.screen

import com.jamieadkins.gwent.domain.card.model.GwentCard

data class CardDatabaseScreenModel(val cards: List<GwentCard>, val searchQuery: String, val updateAvailable: Boolean)