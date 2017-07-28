package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * UI fragment that shows a list of the users decks.
 */

class DeckListFragment : BaseFragment(), DecksContract.View, NewDeckDialog.NewDeckDialogListener {
    private var mDecksPresenter: DecksContract.Presenter? = null

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
            dialog?.setPresenter(mDecksPresenter)
        }

        if (!mPublicDecks) {
            buttonNewDeck.setOnClickListener {
                val newFragment = NewDeckDialog()
                newFragment.setPresenter(mDecksPresenter)
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

    override fun onStart() {
        super.onStart()
        onLoadData()
    }

    override fun onLoadData() {
        super.onLoadData()
        val decks: Observable<RxDatabaseEvent<Deck>>?
        if (!mPublicDecks) {
            decks = mDecksPresenter?.userDecks
        } else {
            decks = mDecksPresenter?.publicDecks
        }
        decks?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(observer)

        if (mPublicDecks) {
            mDecksPresenter?.deckOfTheWeek
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : BaseSingleObserver<RxDatabaseEvent<Deck>>() {
                        override fun onSuccess(value: RxDatabaseEvent<Deck>) {
                            recyclerViewAdapter.addItem(0, SubHeader("Deck of the Week"))
                            recyclerViewAdapter.addItem(1, value.value)
                            recyclerViewAdapter.addItem(2, SubHeader("Featured Decks"))
                        }
                    })
        }
    }

    override fun onStop() {
        super.onStop()
        mDecksPresenter?.stop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(STATE_PUBLIC_DECKS, mPublicDecks)
        super.onSaveInstanceState(outState)
    }

    override fun setPresenter(presenter: DecksContract.Presenter) {
        mDecksPresenter = presenter
    }

    override fun createNewDeck(name: String, faction: String, leader: CardDetails) {
        mDecksPresenter?.createNewDeck(name, faction, leader, "v0-8-60-2-images")
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : BaseObserver<RxDatabaseEvent<Deck>>() {
                    override fun onNext(value: RxDatabaseEvent<Deck>) {
                        val deck = value.value

                        val intent = Intent(activity, UserDeckDetailActivity::class.java)
                        intent.putExtra(UserDeckDetailActivity.EXTRA_DECK_ID, deck.id)
                        intent.putExtra(UserDeckDetailActivity.EXTRA_FACTION_ID, deck.factionId)
                        intent.putExtra(UserDeckDetailActivity.EXTRA_IS_PUBLIC_DECK, deck.isPublicDeck)
                        view?.context?.startActivity(intent)

                        FirebaseUtils.logAnalytics(view?.context,
                                deck.factionId, deck.name, "Create Deck")
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun setLoadingIndicator(active: Boolean) {
        isLoading = active
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
