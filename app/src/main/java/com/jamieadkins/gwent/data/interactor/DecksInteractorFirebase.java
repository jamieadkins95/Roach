package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.deck.DecksContract;

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

public class DecksInteractorFirebase implements DecksInteractor {
    private DecksContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mDecksReference;
    private final Query mDecksQuery;
    private ChildEventListener mDeckListener;

    private final String databasePath;

    public DecksInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePath = "users/" + userId + "/decks/";
        mDecksReference = mDatabase.getReference(databasePath);
        mDecksQuery = mDecksReference.orderByChild("name");
    }

    @Override
    public void setPresenter(DecksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDecks() {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        mDeckListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                mPresenter.onLoadingComplete();
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.getValue(Deck.class),
                                                RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.getValue(Deck.class),
                                                RxDatabaseEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.getValue(Deck.class),
                                                RxDatabaseEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                dataSnapshot.getValue(Deck.class),
                                                RxDatabaseEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDecksQuery.addChildEventListener(mDeckListener);
                    }
                });
            }
        });
    }

    @Override
    public void stopData() {
        mDecksQuery.removeEventListener(mDeckListener);
    }

    @Override
    public void createNewDeck(String name, String faction) {
        String key = mDecksReference.push().getKey();
        Deck deck = new Deck(key, name, faction);
        Map<String, Object> deckValues = deck.toMap();

        Map<String, Object> firebaseUpdates = new HashMap<>();
        firebaseUpdates.put(key, deckValues);

        mDecksReference.updateChildren(firebaseUpdates);
    }

    @Override
    public void addCardToDeck(Deck deck, final String cardId) {
        DatabaseReference deckReference = mDecksReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                if (storedDeck.getCards() == null) {
                    // First card being added to the deck!
                    storedDeck.initialiseCardMap();
                }

                if (storedDeck.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedDeck.getCards().get(cardId);
                    storedDeck.getCards().put(cardId, currentCardCount + 1);
                } else {
                    // Else add one card to the deck.
                    storedDeck.getCards().put(cardId, 1);
                }

                // Set value and report transaction success.
                mutableData.setValue(storedDeck);
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
    public void removeCardFromDeck(Deck deck, final String cardId) {
        DatabaseReference deckReference = mDecksReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                if (storedDeck.getCards() == null) {
                    // Fresh deck that has no cards in it!
                    return Transaction.success(mutableData);
                }

                if (storedDeck.getCards().containsKey(cardId)) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedDeck.getCards().get(cardId);
                    storedDeck.getCards().put(cardId, currentCardCount - 1);
                } else {
                    // This deck doesn't have that card in it.
                }

                // Set value and report transaction success.
                mutableData.setValue(storedDeck);
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
}
