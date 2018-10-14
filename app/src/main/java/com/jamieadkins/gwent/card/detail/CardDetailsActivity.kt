package com.jamieadkins.gwent.card.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.detail.CardDetailsFragment.Companion.KEY_ID

class CardDetailsActivity : AppCompatActivity() {

    lateinit var cardId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        cardId = savedInstanceState?.getString(KEY_ID)
                ?: intent?.getStringExtra(CardDetailsFragment.KEY_ID)
                ?: throw Exception("Card ID not found")

        if (savedInstanceState == null) {
            val fragment = CardDetailsFragment()
            val bundle = Bundle()
            bundle.putString(CardDetailsFragment.KEY_ID, cardId)
            fragment.arguments = bundle
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CardDetailsFragment.KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }

}