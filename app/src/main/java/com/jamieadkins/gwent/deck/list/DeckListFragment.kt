package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.commonutils.mvp2.MvpFragment

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.commonutils.ui.SubHeader
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseFragment
import com.jamieadkins.gwent.base.BaseObserver
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.card.detail.DetailActivity
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.FirebaseUtils
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * UI fragment that shows a list of the users decks.
 */

class DeckListFragment : BaseFragment<DecksContract.View>(), DecksContract.View, NewDeckDialog.NewDeckDialogListener {

    // Set up to show user decks by default.
    private var mPublicDecks = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mPublicDecks = savedInstanceState.getBoolean(STATE_PUBLIC_DECKS)
        }

        if (!mPublicDecks) {
            activity.title = getString(R.string.my_decks)
        } else {
            activity.title = getString(R.string.public_decks)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_deck_list, container, false)

        setupViews(rootView)

        val buttonNewDeck = rootView?.findViewById<View>(R.id.new_deck) as FloatingActionButton

        if (savedInstanceState != null) {
            val dialog = activity.supportFragmentManager
                    .findFragmentByTag(NewDeckDialog::class.java.simpleName) as NewDeckDialog?
            dialog?.setPresenter(presenter as DecksContract.Presenter)
        }

        if (!mPublicDecks) {
            buttonNewDeck.setOnClickListener {
                val newFragment = NewDeckDialog()
                newFragment.setPresenter(presenter as DecksContract.Presenter)
                newFragment.setTargetFragment(this@DeckListFragment, REQUEST_CODE)
                newFragment.show(activity.supportFragmentManager,
                        newFragment.javaClass.simpleName)
            }
        } else {
            buttonNewDeck.visibility = View.GONE
            buttonNewDeck.isEnabled = false
        }

        return rootView
    }

    override fun setupPresenter() {
        presenter = DecksPresenter(DecksInteractorFirebase(), CardsInteractorFirebase.instance)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(STATE_PUBLIC_DECKS, mPublicDecks)
        super.onSaveInstanceState(outState)
    }

    override fun createNewDeck(name: String, faction: String, leader: CardDetails) {
        (presenter as DecksContract.Presenter).createNewDeck(name, faction, leader, "")
    }

    override fun setLoadingIndicator(active: Boolean) {

    }

    companion object {
        private val REQUEST_CODE = 3414
        private val STATE_PUBLIC_DECKS = "com.jamieadkins.gwent.user.decks"

        fun newInstance(userDecks: Boolean): DeckListFragment {
            val fragment = DeckListFragment()
            fragment.mPublicDecks = userDecks
            return fragment
        }
    }
}
