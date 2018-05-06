package com.jamieadkins.gwent.card.detail

import com.jamieadkins.gwent.core.GwentCard

data class CardDetailsScreenData(val card: GwentCard, val relatedCards: List<GwentCard> = emptyList())