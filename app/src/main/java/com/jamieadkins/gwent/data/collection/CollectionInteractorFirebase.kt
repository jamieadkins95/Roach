package com.jamieadkins.gwent.data.collection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.jamieadkins.gwent.data.FirebaseUtils
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

import java.util.HashMap

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Deals with firebase.
 */

class CollectionInteractorFirebase : CollectionInteractor {
    private val mDatabase = FirebaseUtils.getDatabase()
    private val mCollectionReference: DatabaseReference
    private var mCollectionListener: ChildEventListener? = null

    private val databasePath: String

    init {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        databasePath = "users/$userId/collection/"
        mCollectionReference = mDatabase.getReference(databasePath)
    }

    override fun addCardToCollection(cardId: String, variationId: String) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var storedCollection: Collection? = mutableData.getValue<Collection>(Collection::class.java)

                // User doesn't have a collection yet.
                if (storedCollection == null) {
                    storedCollection = Collection()
                }

                var variations = storedCollection.cards[cardId]
                if (variations != null) {
                    // If the user already has at least one of these cards in their deck.
                    val currentCardCount = variations[variationId]
                    if (currentCardCount != null) {
                        // If they already have at least one of these variations.
                        variations.put(variationId, currentCardCount + 1)
                    } else {
                        variations.put(variationId, 1)
                    }

                    storedCollection.cards.put(cardId, variations)

                } else {
                    // User is adding first variation of this card.
                    variations = HashMap<String, Int>()
                    variations.put(variationId, 1)
                    storedCollection.cards.put(cardId, variations)
                }

                // Set value and report transaction success.
                mutableData.value = storedCollection
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                    dataSnapshot: DataSnapshot) {
                // Do nothing.
            }
        })
    }

    override fun removeCardFromCollection(cardId: String, variationId: String) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val storedCollection = mutableData.getValue<Collection>(Collection::class.java) ?: return Transaction.success(mutableData)

                val variations = storedCollection.cards[cardId]
                if (variations != null) {
                    // If the user already has at least one of these cards in their deck.
                    val currentCardCount = variations[variationId]
                    if (currentCardCount != null) {
                        // If they already have at least one of these variations.
                        variations.put(variationId, Math.max(currentCardCount - 1, 0))
                        storedCollection.cards.put(cardId, variations)
                    } else {
                        // This collection doesn't have that variation in it.
                    }
                } else {
                    // This collection doesn't have that card in it.
                }

                // Set value and report transaction success.
                mutableData.value = storedCollection
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                    dataSnapshot: DataSnapshot) {
                // Do nothing.
            }
        })
    }

    override fun getCollection(): Observable<RxDatabaseEvent<Map<String, Long>>> {
        return Observable.defer {
            Observable.create(ObservableOnSubscribe<RxDatabaseEvent<Map<String, Long>>> { emitter ->
                mCollectionListener = object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        val cards = dataSnapshot.value as Map<String, Long>
                        emitter.onNext(RxDatabaseEvent(
                                dataSnapshot.key,
                                cards,
                                RxDatabaseEvent.EventType.ADDED))
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                        val cards = dataSnapshot.value as Map<String, Long>
                        emitter.onNext(RxDatabaseEvent(
                                dataSnapshot.key,
                                cards,
                                RxDatabaseEvent.EventType.CHANGED))
                    }

                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                        val cards = dataSnapshot.value as Map<String, Long>
                        emitter.onNext(RxDatabaseEvent(
                                dataSnapshot.key,
                                cards,
                                RxDatabaseEvent.EventType.REMOVED))
                    }

                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                        val cards = dataSnapshot.value as Map<String, Long>
                        emitter.onNext(RxDatabaseEvent(
                                dataSnapshot.key,
                                cards,
                                RxDatabaseEvent.EventType.MOVED))
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {

                    }
                }

                mCollectionReference.child("cards").addChildEventListener(mCollectionListener)
            })
        }
    }

    override fun stopCollectionUpdates() {
        mCollectionListener?.let {
            mCollectionReference.removeEventListener(it)
        }
    }
}
