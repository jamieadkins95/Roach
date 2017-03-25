package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.FirebaseUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

/**
 * Deals with firebase.
 */

public class CollectionInteractorFirebase implements CollectionInteractor {
    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mCollectionReference;
    private ChildEventListener mCollectionListener;

    private final String databasePath;

    public CollectionInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePath = "users/" + userId + "/collection/";
        mCollectionReference = mDatabase.getReference(databasePath);
    }

    @Override
    public void addCardToCollection(final String cardId, final String variationId) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Collection storedCollection = mutableData.getValue(Collection.class);

                // User doesn't have a collection yet.
                if (storedCollection == null) {
                    storedCollection = new Collection();
                }

                if (storedCollection.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    Map<String, Integer> variations = storedCollection.getCards().get(cardId);

                    if (storedCollection.getCards().get(cardId).containsKey(variationId)) {
                        // If they already have at least one of these variations.
                        int currentCardCount = variations.get(variationId);
                        variations.put(variationId, currentCardCount + 1);
                    } else {
                        variations.put(variationId, 1);
                    }

                    storedCollection.getCards().put(cardId, variations);

                } else {
                    // User is adding first variation of this card.
                    Map<String, Integer> variations = new HashMap<>();
                    variations.put(variationId, 1);
                    storedCollection.getCards().put(cardId, variations);
                }

                // Set value and report transaction success.
                mutableData.setValue(storedCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    @Override
    public void removeCardFromCollection(final String cardId, final String variationId) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Collection storedCollection = mutableData.getValue(Collection.class);

                // User doesn't have a collection yet.
                if (storedCollection == null) {
                    return Transaction.success(mutableData);
                }

                if (storedCollection.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    Map<String, Integer> variations = storedCollection.getCards().get(cardId);

                    if (storedCollection.getCards().get(cardId).containsKey(variationId)) {
                        // If they already have at least one of these variations.
                        int currentCardCount = variations.get(variationId);
                        // Can't have a negative card amount.
                        variations.put(variationId, Math.max(currentCardCount - 1, 0));
                        storedCollection.getCards().put(cardId, variations);
                    } else {
                        // This collection doesn't have that variation in it.
                    }
                } else {
                    // This collection doesn't have that card in it.
                }

                // Set value and report transaction success.
                mutableData.setValue(storedCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Do nothing.
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Map<String, Long>>> getCollection() {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Map<String, Long>>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Map<String, Long>>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Map<String, Long>>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Map<String, Long>>> emitter) throws Exception {
                        mCollectionListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Map<String, Long> cards = (Map<String, Long>) dataSnapshot.getValue();
                                emitter.onNext(new RxDatabaseEvent<>(
                                        dataSnapshot.getKey(),
                                        cards,
                                        RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                Map<String, Long> cards = (Map<String, Long>) dataSnapshot.getValue();
                                emitter.onNext(new RxDatabaseEvent<>(
                                        dataSnapshot.getKey(),
                                        cards,
                                        RxDatabaseEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                Map<String, Long> cards = (Map<String, Long>) dataSnapshot.getValue();
                                emitter.onNext(new RxDatabaseEvent<>(
                                        dataSnapshot.getKey(),
                                        cards,
                                        RxDatabaseEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                Map<String, Long> cards = (Map<String, Long>) dataSnapshot.getValue();
                                emitter.onNext(new RxDatabaseEvent<>(
                                        dataSnapshot.getKey(),
                                        cards,
                                        RxDatabaseEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mCollectionReference.child("cards").addChildEventListener(mCollectionListener);
                    }
                });
            }
        });
    }

    @Override
    public void stopCollectionUpdates() {
        mCollectionReference.removeEventListener(mCollectionListener);
    }
}
