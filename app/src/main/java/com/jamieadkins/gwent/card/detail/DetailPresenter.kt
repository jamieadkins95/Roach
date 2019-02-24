package com.jamieadkins.gwent.card.detail

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.domain.card.GetCardUseCase
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val view: DetailContract.View,
    private val getCardUseCase: GetCardUseCase,
    private val getCardsUseCase: GetCardsUseCase
) : BasePresenter(), DetailContract.Presenter {

    private val cardId = BehaviorSubject.create<String>()

    override fun onAttach() {
        cardId
            .switchMap { getCardUseCase.getCard(it) }
            .switchMap { card ->
                getCardsUseCase.getCards(card.relatedCards)
                    .map { related ->
                        CardDetailsScreenData(card, related.toList())
                    }
            }
            .doOnSubscribe { view.showLoadingIndicator() }
            .subscribeWith(object : BaseDisposableObserver<CardDetailsScreenData>() {
                override fun onNext(result: CardDetailsScreenData) {
                    view.showScreen(result)
                    view.hideLoadingIndicator()
                }
            })
            .addToComposite()
    }

    override fun setCardId(cardId: String) = this.cardId.onNext(cardId)
}
