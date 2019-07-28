package com.jamieadkins.gwent.deck.detail.leader

import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.LeaderPickerEvent
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.ChangeLeaderUseCase
import com.jamieadkins.gwent.domain.deck.GetDeckUseCase
import com.jamieadkins.gwent.domain.deck.GetLeadersUseCase
import com.jamieadkins.gwent.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class LeaderPickerPresenter @Inject constructor(
    private val view: LeaderPickerContract.View,
    private val getDeckUseCase: GetDeckUseCase,
    private val getLeadersUseCase: GetLeadersUseCase,
    private val changeLeaderUseCase: ChangeLeaderUseCase
) : BasePresenter(), LeaderPickerContract.Presenter {

    private val latestDeckId = BehaviorSubject.create<String>()

    override fun onAttach() {
        latestDeckId
            .switchMap(getDeckUseCase::get)
            .switchMap { deck -> getLeadersUseCase.get(deck.faction) }
            .subscribeWith(object : BaseDisposableObserver<List<GwentCard>>() {
                override fun onNext(leaders: List<GwentCard>) {
                    view.showLeaders(leaders)
                }
            })
            .addToComposite()

        Observable.combineLatest(
            latestDeckId,
            RxBus.register(LeaderPickerEvent::class.java),
            BiFunction { deckId: String, leaderEvent: LeaderPickerEvent -> Pair(deckId, leaderEvent.data) })
            .flatMapCompletable {
                changeLeaderUseCase.change(it.first, it.second)
                    .doOnComplete { view.close() }
            }
            .subscribeWith(object : BaseCompletableObserver() {
                override fun onComplete() {
                    // This is will never complete.
                }
            })
            .addToComposite()
    }

    override fun setDeckId(deckId: String) {
        latestDeckId.onNext(deckId)
    }
}
