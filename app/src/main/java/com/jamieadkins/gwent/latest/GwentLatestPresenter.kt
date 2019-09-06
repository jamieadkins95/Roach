package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.domain.latest.GetLatestNewsUseCase
import com.jamieadkins.gwent.domain.latest.GetLatestPatchNotesUseCase
import com.jamieadkins.gwent.domain.latest.GwentNewsArticle
import javax.inject.Inject

class GwentLatestPresenter @Inject constructor(
    private val view: GwentLatestContract.View,
    private val getLatestPatchNotesUseCase: GetLatestPatchNotesUseCase,
    private val getLatestNewsUseCase: GetLatestNewsUseCase
) : BasePresenter(), GwentLatestContract.Presenter {

    override fun onAttach() {
        getLatestPatchNotesUseCase.getPatchNotes()
            .subscribeWith(object : BaseDisposableObserver<GwentNewsArticle>() {
                override fun onNext(data: GwentNewsArticle) {
                    view.showLatestPatchNotes(data)
                }
            })
            .addToComposite()

        getLatestNewsUseCase.getLatestNews()
            .subscribeWith(object : BaseDisposableObserver<List<GwentNewsArticle>>() {
                override fun onNext(data: List<GwentNewsArticle>) {
                    view.showLatestNews(data)
                }
            })
            .addToComposite()
    }
}
