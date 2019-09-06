package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.base.MvpPresenter
import com.jamieadkins.gwent.domain.latest.GwentNewsArticle

interface GwentLatestContract {
    interface View {

        fun showLatestPatchNotes(patchNotes: GwentNewsArticle)

        fun showLatestNews(news: List<GwentNewsArticle>)

        fun showLiveOnTwitch(live: Boolean)
    }

    interface Presenter : MvpPresenter
}
