package com.jamieadkins.gwent.deck.create

import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.deck.CreateDeckUseCase
import com.jamieadkins.gwent.main.BasePresenter
import javax.inject.Inject

class CreateDeckPresenter @Inject constructor(
    private val view: CreateDeckContract.View,
    private val createDeckUseCase: CreateDeckUseCase
) : BasePresenter(), CreateDeckContract.Presenter {

    override fun onAttach() {
        // Do nothing.
    }

    override fun createDeck(name: String, faction: GwentFaction) {
        createDeckUseCase.create(name, faction)
            .subscribeWith(object : BaseDisposableSingle<String>() {
                override fun onSuccess(newDeckId: String) {
                    view.showDeckDetails(newDeckId)
                    view.close()
                }
            })
            .addToComposite()
    }
}
