package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.collection.CollectionContract;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.FirebaseUtils;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

/**
 * Deals with firebase.
 */

public class CollectionInteractorFirebase implements CollectionInteractor {
    private CollectionContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mCollectionReference;
    private ValueEventListener mCollectionListener;

    private final String databasePath;

    public CollectionInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePath = "users/" + userId + "/collection/";
        mCollectionReference = mDatabase.getReference(databasePath);
    }

    @Override
    public void setPresenter(CollectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addCardToCollection(final String cardId) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Collection storedCollection = mutableData.getValue(Collection.class);

                if (storedCollection.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedCollection.getCards().get(cardId);
                    storedCollection.getCards().put(cardId, currentCardCount + 1);
                } else {
                    // Else add one card to the deck.
                    storedCollection.getCards().put(cardId, 1);
                }

                // Set value and report transaction success.
                mutableData.setValue(storedCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(getClass().getSimpleName(), "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public void removeCardFromCollection(final String cardId) {
        // Transactions will ensure concurrency errors don't occur.
        mCollectionReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Collection storedCollection = mutableData.getValue(Collection.class);

                if (storedCollection.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedCollection.getCards().get(cardId);
                    // Can't have a negative card amount.
                    storedCollection.getCards().put(cardId, Math.max(currentCardCount - 1, 0));
                } else {
                    // This deck doesn't have that card in it.
                }

                // Set value and report transaction success.
                mutableData.setValue(storedCollection);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(getClass().getSimpleName(), "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public Observable<Collection> getCollection() {
        return Observable.defer(new Callable<ObservableSource<? extends Collection>>() {
            @Override
            public ObservableSource<? extends Collection> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<Collection>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Collection> emitter) throws Exception {
                        mCollectionListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Collection collection = dataSnapshot.getValue(Collection.class);
                                emitter.onNext(collection);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mCollectionReference.addValueEventListener(mCollectionListener);
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
