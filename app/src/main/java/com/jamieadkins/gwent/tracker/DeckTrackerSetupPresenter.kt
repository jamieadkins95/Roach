package com.jamieadkins.gwent.tracker

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.GetLeadersUseCase
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DeckTrackerSetupPresenter @Inject constructor(
    private val view: DeckTrackerSetupContract.View,
    private val getLeadersUseCase: GetLeadersUseCase
) : BasePresenter(), DeckTrackerSetupContract.Presenter {

    private val selectedFaction = BehaviorSubject.create<GwentFaction>()

    override fun onAttach() {
        selectedFaction
            .switchMap { getLeadersUseCase.get(it) }
            .subscribeWith(object : BaseDisposableObserver<List<GwentCard>>() {
                override fun onNext(cards: List<GwentCard>) {
                    view.showLeaders(cards)
                }
            })
            .addToComposite()
    }

    override fun onFactionSelected(faction: GwentFaction) {
        selectedFaction.onNext(faction)
    }

    override fun onLeaderSelected(cardId: String) {
        selectedFaction.value?.let { view.launchDeckTracker(it, cardId) }
    }
}
