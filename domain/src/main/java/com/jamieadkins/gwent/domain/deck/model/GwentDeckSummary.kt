package com.jamieadkins.gwent.domain.deck.model

import com.jamieadkins.gwent.domain.card.model.GwentCard

data class GwentDeckSummary(val deck: GwentDeck,
                            val leader: GwentCard,
                            val cardCounts: GwentDeckCardCounts)