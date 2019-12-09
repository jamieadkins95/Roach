package com.jamieadkins.gwent.domain.card.model

sealed class GwentExpansion {

    object Token : GwentExpansion()

    object Base : GwentExpansion()

    object Unmillable : GwentExpansion()

    object Thronebreaker : GwentExpansion()

    object CrimsonCurse : GwentExpansion()

    object Novigrad : GwentExpansion()

    object IronJudgement : GwentExpansion()

    object MerchantsOfOfir : GwentExpansion()
}
