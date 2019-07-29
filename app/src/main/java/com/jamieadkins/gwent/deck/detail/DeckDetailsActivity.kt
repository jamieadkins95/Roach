package com.jamieadkins.gwent.deck.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import com.jamieadkins.gwent.di.DaggerAppComponent
import com.jamieadkins.gwent.base.DaggerAndroidActivity

class DeckDetailsActivity : DaggerAndroidActivity() {

    override fun onInject() {
        DaggerAppComponent.builder()
            .core(coreComponent)
            .build()
            .inject(this)
    }

    lateinit var deckId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        deckId = savedInstanceState?.getString(DeckDetailsFragment.KEY_ID)
                ?: intent?.getStringExtra(DeckDetailsFragment.KEY_ID)
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

    companion object {

        fun getIntent(context: Context, deckId: String): Intent {
            val intent = Intent(context, DeckDetailsActivity::class.java)
            intent.putExtra(DeckDetailsFragment.KEY_ID, deckId)
            return intent
        }
    }

}