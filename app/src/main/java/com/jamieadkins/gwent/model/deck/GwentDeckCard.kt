package com.jamieadkins.gwent.model.deck

import com.jamieadkins.gwent.model.GwentCard

data class GwentDeckCard(
        val card: GwentCard,
        val count: Int = 0
)