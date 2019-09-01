package com.jamieadkins.gwent.deckbuilder

import android.os.Bundle
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.deckbuilder.di.DaggerDeckBuilderComponent
import com.jamieadkins.gwent.base.DaggerAndroidActivity

class DeckDetailsActivity : DaggerAndroidActivity() {

    override fun onInject() {
        DaggerDeckBuilderComponent.builder()
            .core(coreComponent)
            .build()
            .inject(this)
    }

    lateinit var deckId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_detail)

        deckId = savedInstanceState?.getString(DeckDetailsFragment.KEY_ID)
                ?: intent?.data?.getQueryParameter("deckId")
                ?: throw Exception("Deck ID not found")

        if (savedInstanceState == null) {
            val fragment = DeckDetailsFragment()
            val bundle = Bundle()
            bundle.putString(DeckDetailsFragment.KEY_ID, deckId)
            fragment.arguments = bundle
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DeckDetailsFragment.KEY_ID, deckId)
        super.onSaveInstanceState(outState)
    }

}