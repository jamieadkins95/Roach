package com.jamieadkins.gwent.card.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.update.UpdateActivity

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
        val message = getString(R.string.update_available)
        val actionMessage = getString(R.string.update)
        val action = View.OnClickListener {
            val i = Intent(activity, UpdateActivity::class.java)
            startActivity(i)
        }
        RxBus.post(SnackbarRequest(SnackbarBundle(message, actionMessage, action, Snackbar.LENGTH_INDEFINITE)))
    }
}
