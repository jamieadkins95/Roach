package com.jamieadkins.gwent.card.detail

import android.os.Bundle
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.card.detail.CardDetailsFragment.Companion.KEY_ID
import com.jamieadkins.gwent.di.DaggerAppComponent
import com.jamieadkins.gwent.base.DaggerAndroidActivity

class CardDetailsActivity : DaggerAndroidActivity() {

    lateinit var cardId: String

    override fun onInject() {
        DaggerAppComponent.builder()
            .core(coreComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        cardId = savedInstanceState?.getString(KEY_ID)
                ?: intent?.getStringExtra(KEY_ID)
                ?: throw Exception("Card ID not found")

        if (savedInstanceState == null) {
            val fragment = CardDetailsFragment()
            val bundle = Bundle()
            bundle.putString(KEY_ID, cardId)
            fragment.arguments = bundle
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }

}