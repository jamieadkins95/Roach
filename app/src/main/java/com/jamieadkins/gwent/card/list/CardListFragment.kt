package com.jamieadkins.gwent.card.list

import android.os.Bundle

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R

/**
 * UI fragment that shows a list of the users decks.
 */

class CardListFragment : BaseCardListFragment<CardsContract.View>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.card_database)
    }

    override fun setupPresenter() {
        presenter = CardsPresenter(Injection.provideSchedulerProvider(),
                Injection.provideCardRepository(),
                Injection.provideUpdateRepository())
    }
}
