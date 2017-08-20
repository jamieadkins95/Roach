package com.jamieadkins.gwent.data.interactor

import com.google.firebase.database.*
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.FirebaseUtils
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

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
        mPatchReference.keepSynced(true)
        mMistakesReference = mDatabase.getReference("reported-mistakes")
    }

    private fun onPatchUpdated(patch: String?) {
        patch.let {
            mCardsReference = mDatabase.getReference("card-data/" + patch)
            // Keep Cards data in cache at all times.
            mCardsReference?.keepSynced(true)
        }
    }

    private val latestPatch: Single<String>
        get() = Single.defer {
            Single.create(SingleOnSubscribe<String> { emitter ->
                val patchListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val patch = dataSnapshot.getValue(String::class.java)
                        patch?.let {
                            onPatchUpdated(patch)
                            emitter.onSuccess(patch)
                        }
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
        private val PATCH_PATH = "patch/" + BuildConfig.CARD_DATA_VERSION
        val instance: CardsInteractorFirebase by lazy { CardsInteractorFirebase() }
    }
}
