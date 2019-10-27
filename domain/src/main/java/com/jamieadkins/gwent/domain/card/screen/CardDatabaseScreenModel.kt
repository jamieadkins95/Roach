package com.jamieadkins.gwent.domain.card.screen

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.update.model.Notice

data class CardDatabaseScreenModel(
    val cards: List<GwentCard>,
    val searchQuery: String,
    val notices: List<Notice>,
    val showInstantAppNotice: Boolean
)