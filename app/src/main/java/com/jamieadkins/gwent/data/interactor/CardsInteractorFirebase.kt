package com.jamieadkins.gwent.data.interactor

import android.util.Log

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.FirebaseUtils

import java.util.HashMap
import java.util.concurrent.Callable

import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.SingleSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Deals with firebase.
 */

class CardsInteractorFirebase private constructor() : CardsInteractor {

    private val mDatabase = FirebaseUtils.getDatabase()
    private var mCardsReference: DatabaseReference? = null
    private val mMistakesReference: DatabaseReference
    private val mPatchReference: DatabaseReference

    private var mCardsQuery: Query? = null
    private var mCardListener: ValueEventListener? = null
    private var mLocale = "en-US"

    init {
        mPatchReference = mDatabase.getReference(PATCH_PATH)
        mMistakesReference = mDatabase.getReference("reported-mistakes")
    }

    private fun onPatchUpdated(patch: String) {
        mCardsReference = mDatabase.getReference("card-data/" + patch)
        // Keep Cards data in cache at all times.
        mCardsReference?.keepSynced(true)
    }

    private val latestPatch: Single<String>
        get() = Single.defer {
            Single.create(SingleOnSubscribe<String> { emitter ->
                val patchListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val patch = dataSnapshot.getValue(String::class.java)
                        onPatchUpdated(patch)
                        emitter.onSuccess(patch)
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {

                    }
                }

                mPatchReference.addListenerForSingleValueEvent(patchListener)
            })
        }

    override fun setLocale(locale: String) {
        mLocale = locale
    }

    override fun getCards(filter: CardFilter): Observable<RxDatabaseEvent<CardDetails>> {
        return latestPatch.flatMapObservable { patch ->
            onPatchUpdated(patch)
            mCardsQuery = mCardsReference!!.orderByChild("localisedData/name/" + mLocale)

            if (filter.searchQuery != null) {
                val query = filter.searchQuery
                val charValue = query[query.length - 1].toInt()
                var endQuery = query.substring(0, query.length - 1)
                endQuery += (charValue + 1).toChar()

                mCardsQuery = mCardsQuery!!.startAt(query)
                        // No 'contains' query so have to fudge it.
                        .endAt(endQuery)
            }
            cardDataSnapshot.flatMapObservable { dataSnapshot ->
                Observable.create(ObservableOnSubscribe<RxDatabaseEvent<CardDetails>> { emitter ->
                    for (cardSnapshot in dataSnapshot.children) {
                        val cardDetails = cardSnapshot.getValue(CardDetails::class.java)

                        if (cardDetails == null) {
                            emitter.onError(Throwable("Card doesn't exist."))
                            emitter.onComplete()
                            return@ObservableOnSubscribe
                        }

                        // Only add card if the card meets all the filters.
                        // Also check name and info are not null. Those are dud cards.
                        if (filter.doesCardMeetFilter(cardDetails)) {
                            emitter.onNext(
                                    RxDatabaseEvent(
                                            cardSnapshot.key,
                                            cardDetails,
                                            RxDatabaseEvent.EventType.ADDED
                                    ))
                        }
                    }

                    emitter.onComplete()
                })
                        // IMPORTANT: Firebase forces us back onto UI thread.
                        .subscribeOn(Schedulers.io())
            }
        }
    }

    private val cardDataSnapshot: Single<DataSnapshot>
        get() = Single.defer {
            Single.create(SingleOnSubscribe<DataSnapshot> { emitter ->
                mCardListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        emitter.onSuccess(dataSnapshot)
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {

                    }
                }

                mCardsQuery!!.addListenerForSingleValueEvent(mCardListener)
            })
        }

    /**
     * We have a separate method for getting one card as it is significantly quicker than using a
     * card filter.

     * @param id id of the card to retrieve
     */
    override fun getCard(id: String): Single<RxDatabaseEvent<CardDetails>> {
        return latestPatch.flatMap { patch ->
            onPatchUpdated(patch)
            Single.defer {
                Single.create(SingleOnSubscribe<RxDatabaseEvent<CardDetails>> { emitter ->
                    mCardsQuery = mCardsReference!!.child(id)

                    mCardListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val cardDetails = dataSnapshot.getValue(CardDetails::class.java)

                            if (cardDetails == null) {
                                emitter.onError(Throwable("Card doesn't exist."))
                                return
                            }

                            emitter.onSuccess(
                                    RxDatabaseEvent(
                                            dataSnapshot.key,
                                            cardDetails,
                                            RxDatabaseEvent.EventType.ADDED
                                    ))
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {

                        }
                    }

                    mCardsQuery!!.addListenerForSingleValueEvent(mCardListener)
                })
            }
        }
    }

    override fun reportMistake(cardid: String, description: String): Completable {
        return Completable.defer {
            Completable.create { emitter ->
                val key = mMistakesReference.push().key
                val values = HashMap<String, Any>()
                values.put("cardId", cardid)
                values.put("description", description)
                values.put("fixed", false)

                val firebaseUpdates = HashMap<String, Any>()
                firebaseUpdates.put(key, values)

                mMistakesReference.updateChildren(firebaseUpdates)

                emitter.onComplete()
            }
        }
    }

    override fun removeListeners() {
        if (mCardsQuery != null) {
            mCardsQuery!!.removeEventListener(mCardListener!!)
        }
    }

    companion object {
        private val PATCH_PATH = if (!BuildConfig.DEBUG) "card-data/latest-patch" else "card-data/latest-patch-debug"
        val instance: CardsInteractorFirebase by lazy { CardsInteractorFirebase() }
    }
}
