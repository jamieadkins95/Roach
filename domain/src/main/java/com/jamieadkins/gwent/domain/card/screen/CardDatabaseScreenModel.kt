package com.jamieadkins.gwent.domain.card.screen

import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult

data class CardDatabaseScreenModel(val cards: CardDatabaseResult, val updateAvailable: Boolean)