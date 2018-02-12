package com.jamieadkins.gwent.card.list

import android.os.Bundle
import android.support.design.widget.Snackbar

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest

/**
 * UI fragment that shows a list of the users decks.
 */

class CardListFragment : BaseCardListFragment<CardDatabaseContract.View>(), CardDatabaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.card_database)
    }

    override fun setupPresenter() {
        presenter = CardsPresenter(Injection.provideSchedulerProvider(),
                Injection.provideCardRepository(),
                Injection.provideUpdateRepository())
    }

    override fun showUpdateAvailable() {
        RxBus.post(SnackbarRequest(SnackbarBundle("Update Available", Snackbar.LENGTH_LONG)))
    }
}
