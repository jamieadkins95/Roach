package com.jamieadkins.gwent.deckbuilder.trynow

import android.os.Bundle
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerAndroidActivity
import com.jamieadkins.gwent.base.FeatureNavigator
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import javax.inject.Inject

class TryNowActivity : DaggerAndroidActivity(), TryNowContract.View {

    @Inject lateinit var presenter: TryNowContract.Presenter

    override fun onInject() {
        DaggerTryNowComponent.builder()
            .core(coreComponent)
            .activity(this)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach()
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    override fun goToDeck(deckId: String) {
        FeatureNavigator(this).openDeckBuilder(deckId)
        finish()
    }
}