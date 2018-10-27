package com.jamieadkins.gwent.domain.card.model

sealed class GwentCardType {

    object Unit : GwentCardType()

    object Spell : GwentCardType()

    object Artifact : GwentCardType()

    object Leader : GwentCardType()
}
