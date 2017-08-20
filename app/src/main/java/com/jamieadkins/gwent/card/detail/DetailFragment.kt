package com.jamieadkins.gwent.card.detail

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.card.LargeCardView
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.FirebaseUtils
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.trello.rxlifecycle2.components.support.RxFragment

import java.util.ArrayList

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Shows picture and details of a card.
 */

class DetailFragment : RxFragment(), DetailContract.View {
    private var mDetailPresenter: DetailContract.Presenter? = null
    private var mCardPicture: ImageView? = null
    private var mLargeCardView: LargeCardView? = null
    private var mViewPager: ViewPager? = null
    private var mAdapter: CardImagePagerAdapter? = null

    private var mCardId: String? = null

    private var mLocale: String? = null

    private var mCard: CardDetails? = null

    private var mUseLowData = false

    private val mObserver = object : BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
        override fun onSuccess(value: RxDatabaseEvent<CardDetails>) {
            if (activity == null) {
                return
            }

            activity.invalidateOptionsMenu()
            val card = value.value
            mCard = card

            // Update UI with card details.
            activity.title = card.getName(mLocale)

            val storage = FirebaseStorage.getInstance()

            for (variationId in card.variations.keys) {
                val variation = card.variations[variationId]
                var storageReference: StorageReference? = null
                variation?.let {
                    if (mUseLowData) {
                        storageReference = storage.getReferenceFromUrl(it.art?.low ?: "none")
                    } else {
                        storageReference = storage.getReferenceFromUrl(it.art?.medium ?: "none")
                    }
                }

                mAdapter?.addItem(storageReference)
            }

            mLargeCardView?.setCardDetails(card)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mCardId = savedInstanceState.getString(STATE_CARD_ID)
        }

        val key = getString(R.string.pref_locale_key)
        mLocale = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, getString(R.string.default_locale))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_detail, container, false)
        mCardPicture = rootView?.findViewById<View>(R.id.card_image) as ImageView
        mLargeCardView = rootView.findViewById<View>(R.id.card_details) as LargeCardView

        mViewPager = rootView.findViewById<View>(R.id.pager) as ViewPager
        val tabLayout = rootView.findViewById<View>(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(mViewPager, true)

        mAdapter = CardImagePagerAdapter(activity)
        mViewPager?.adapter = mAdapter

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        mUseLowData = preferences.getBoolean(getString(R.string.pref_low_data_key), false)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        mCard?.let {
            inflater?.inflate(R.menu.card_detail, menu)
            menu?.findItem(R.id.action_related)?.isVisible = it.related != null && BuildConfig.DEBUG
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_related -> {
                    mCard?.let {
                        val relatedCards = ArrayList<String>()
                        relatedCards.addAll(it.related)
                        val fragment = CardListBottomSheetFragment.newInstance(relatedCards)
                        fragment.setPresenter(mDetailPresenter)
                        fragment.show(childFragmentManager, "related")
                        return true
                    }
                }
                R.id.action_flag_error -> {
                    val builder = AlertDialog.Builder(activity)
                    val inflater = activity.layoutInflater
                    val view = inflater.inflate(R.layout.dialog_edit_text, null)
                    val input = view.findViewById<View>(R.id.edit_text) as EditText
                    input.setHint(R.string.error_description)
                    builder.setView(view)
                            .setTitle(R.string.flag_error_title)
                            .setMessage(R.string.flag_error_message)
                            .setPositiveButton(R.string.send) { dialog, which ->
                                mDetailPresenter?.reportMistake(mCardId, input.text.toString())
                                        ?.subscribeOn(Schedulers.io())
                                        ?.observeOn(AndroidSchedulers.mainThread())
                                        ?.subscribe(object : BaseCompletableObserver() {
                                            override fun onComplete() {
                                                RxBus.post(SnackbarRequest(SnackbarBundle(getString(R.string.mistake_reported))))
                                            }
                                        })
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                            .create()
                            .show()
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        mDetailPresenter?.getCard(mCardId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.compose(bindToLifecycle())
                ?.subscribe(mObserver)
    }

    override fun onStop() {
        super.onStop()
        mDetailPresenter?.stop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(STATE_CARD_ID, mCardId)
        super.onSaveInstanceState(outState)
    }

    override fun setLoadingIndicator(active: Boolean) {

    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mDetailPresenter = presenter
    }

    companion object {
        private val STATE_CARD_ID = "com.jamieadkins.gwent.cardid"

        fun newInstance(cardId: String): DetailFragment {
            val fragment = DetailFragment()
            fragment.mCardId = cardId
            return fragment
        }
    }
}
