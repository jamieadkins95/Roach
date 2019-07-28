package com.jamieadkins.gwent.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.base.MvpPresenter

interface DeckTrackerSetupContract {

    interface View {

        fun showLeaders(leaders: List<GwentCard>)

        fun launchDeckTracker(faction: GwentFaction, leaderId: String)
    }

    interface Presenter : MvpPresenter {

        fun onLeaderSelected(cardId: String)

        fun onFactionSelected(faction: GwentFaction)
    }
}
