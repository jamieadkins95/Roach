package com.jamieadkins.gwent.data.interactor

import com.google.firebase.database.*
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.card.CardFilter
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.jamieadkins.commonutils.mvp2.applyComputationSchedulers
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.data.*

/**
 * Deals with firebase.
 */

class CardsInteractorFirebase(val locale: String = "en-US") : CardsInteractor {

    private val mDatabase = FirebaseUtils.getDatabase()
    private var mCardsReference: DatabaseReference? = null
    private val mMistakesReference: DatabaseReference
    private val mPatchReference: DatabaseReference

    private var mCardsQuery: Query? = null
    private var mCardListener: ValueEventListener? = null

    private var currentPatch: String = ""

    init {
        mPatchReference = mDatabase.getReference(PATCH_PATH)
        mPatchReference.keepSynced(true)
        mMistakesReference = mDatabase.getReference("reported-mistakes")
    }

    private fun onPatchUpdated(patch: String?) {
        patch?.let {
            mCardsReference = mDatabase.getReference("card-data/" + patch)
            // Keep Cards data in cache at all times.
            mCardsReference?.keepSynced(true)

            if (currentPatch != patch) {
                CardCache.clear()
            }

            currentPatch = patch
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

    override fun getCards(filter: CardFilter): Single<CardListResult> {
        return getCards(filter, null, null)
    }

    override fun getCards(filter: CardFilter?, cardIds: List<String>): Single<CardListResult> {
        return getCards(filter, null, cardIds)
    }

    override fun getCards(filter: CardFilter?, query: String?): Single<CardListResult> {
        return getCards(filter, query, null)
    }

    private fun getCards(filter: CardFilter?, query: String?, cardIds: List<String>?): Single<CardListResult> {
        var source: Single<MutableList<CardDetails>> = getCards()

        if (query != null) {
            source = getCards().applyComputationSchedulers().flatMap { cardList ->
                val searchResults = searchCards(query, cardList, locale)
                recordSearchQuery(query, searchResults)
                getCards(searchResults)
            }
        } else if (cardIds != null) {
            source = getCards(cardIds)
        }

        filter?.let {
            source = source.applyComputationSchedulers().map { cardList ->
                val iterator = cardList.listIterator()
                while (iterator.hasNext()) {
                    val card = iterator.next()
                    if (!filter.doesCardMeetFilter(card)) iterator.remove()
                }
                cardList
            }
        }

        return Single.defer {
            source.map { content -> CardListResult.Success(content) }
        }
    }

    private fun getCards(cardIds: List<String>): Single<MutableList<CardDetails>> {
        val singles: ArrayList<Single<CardDetails>> = ArrayList()
        cardIds.forEach { singles.add(getCard(it)) }
        return Single.merge(singles).toList()
    }

    private fun getCards(): Single<MutableList<CardDetails>> {
        return latestPatch.flatMap { patch ->
            onPatchUpdated(patch)
            mCardsQuery = mCardsReference!!.orderByChild("name/" + locale)
            cardDataSnapshot.applyComputationSchedulers().flatMap { dataSnapshot ->
                Single.create(SingleOnSubscribe<MutableList<CardDetails>> { emitter ->
                    val cardList = mutableListOf<CardDetails>()
                    for (cardSnapshot in dataSnapshot.children) {
                        val cardDetails = cardSnapshot.getValue(CardDetails::class.java)

                        if (cardDetails == null) {
                            emitter.onError(Throwable("Card doesn't exist."))
                            continue
                        }
                        CardCache.cardsById[cardDetails.ingameId] = cardDetails
                        cardList.add(cardDetails)
                    }

                    emitter.onSuccess(cardList)
                })
                        // IMPORTANT: Firebase forces us back onto UI thread.
                        .applyComputationSchedulers()
            }
        }
    }

    private fun recordSearchQuery(query: String, results: List<String>): String {
        val searchReference = mDatabase.getReference("/search/queries")
        val key = searchReference.push().key
        val firebaseUpdates = HashMap<String, Any>()
        val queryMap = HashMap<String, Any>()
        queryMap.put("query", query)
        queryMap.put("results", results)
        firebaseUpdates.put(key, queryMap)

        searchReference.updateChildren(firebaseUpdates)
        return key
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
    override fun getCard(id: String): Single<CardDetails> {
        if (CardCache.cardsById[id] == null) {
            return latestPatch.flatMap { patch ->
                onPatchUpdated(patch)
                Single.create(SingleOnSubscribe<CardDetails> { emitter ->
                    mCardsQuery = mCardsReference!!.child(id)

                    mCardListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val cardDetails = dataSnapshot.getValue(CardDetails::class.java)

                            if (cardDetails == null) {
                                emitter.onError(Throwable("Card doesn't exist."))
                                return
                            }

                            CardCache.cardsById[id] = cardDetails
                            emitter.onSuccess(cardDetails)
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {

                        }
                    }

                    mCardsQuery!!.addListenerForSingleValueEvent(mCardListener)
                }).applyComputationSchedulers()
            }
        } else {
            return Single.defer {
                Single.just(CardCache.cardsById[id])
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
    }
}
