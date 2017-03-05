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
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.twitter.sdk.android.core.models.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;

/**
 * Deals with firebase.
 */

public class DecksInteractorFirebase implements DecksInteractor {
    private static final String PUBLIC_DECKS_PATH = "public-decks/";

    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private Query mDecksQuery;
    private Query mDeckQuery;
    private final DatabaseReference mPublicReference;
    private DatabaseReference mUserReference;
    private ChildEventListener mDecksListener;
    private ValueEventListener mDeckDetailListener;

    public DecksInteractorFirebase() {
        mPublicReference = mDatabase.getReference(PUBLIC_DECKS_PATH);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mUserReference = mDatabase.getReference("users/" + userId + "/decks/");
            mUserReference.keepSynced(true);
        }
    }

    private Observable<RxDatabaseEvent<Deck>> getDecks(Query query) {
        mDecksQuery = query;
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        ValueEventListener initialCountListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emitter.onNext(RxDatabaseEvent.INITIAL_LOAD_COMPLETE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDecksListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot deckSnapshot, String s) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                deckSnapshot.getKey(),
                                                initialiseDeck(deckSnapshot),
                                                RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot deckSnapshot, String s) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                deckSnapshot.getKey(),
                                                initialiseDeck(deckSnapshot),
                                                RxDatabaseEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot deckSnapshot) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                deckSnapshot.getKey(),
                                                initialiseDeck(deckSnapshot),
                                                RxDatabaseEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot deckSnapshot, String s) {
                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                deckSnapshot.getKey(),
                                                initialiseDeck(deckSnapshot),
                                                RxDatabaseEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                            private Deck initialiseDeck(DataSnapshot deckSnapshot) {
                                Deck deck = deckSnapshot.getValue(Deck.class);

                                deck.getLeader().setPatch(deck.getPatch());
                                for (String cardId : deck.getCards().keySet()) {
                                    deck.getCards().get(cardId).setPatch(deck.getPatch());
                                }

                                return deck;
                            }
                        };

                        mDecksQuery.addChildEventListener(mDecksListener);
                        mDecksQuery.addListenerForSingleValueEvent(initialCountListener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getFeaturedDecks() {
        Query query = mPublicReference.child("decks").orderByChild("week");
        return getDecks(query);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getUserDecks() {
        Query query = mUserReference.orderByChild("name");
        return getDecks(query);
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek() {
        mDeckQuery = mPublicReference.child("deck-of-the-week");
        return Single.defer(new Callable<SingleSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public SingleSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Single.create(new SingleOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final SingleEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        mDeckDetailListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Deck deck = dataSnapshot.getValue(Deck.class);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    return;
                                }

                                deck.getLeader().setPatch(deck.getPatch());
                                for (String cardId : deck.getCards().keySet()) {
                                    deck.getCards().get(cardId).setPatch(deck.getPatch());
                                }

                                emitter.onSuccess(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                deck,
                                                RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDeckQuery.addListenerForSingleValueEvent(mDeckDetailListener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(final String deckId, boolean isPublicDeck) {
        if (isPublicDeck) {
            mDeckQuery = mPublicReference.child("decks").child(deckId);
        } else {
            mDeckQuery = mUserReference.child(deckId);
        }
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Deck>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Deck>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Deck>> emitter) throws Exception {
                        mDeckDetailListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Deck deck = dataSnapshot.getValue(Deck.class);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    emitter.onComplete();
                                    return;
                                }

                                deck.getLeader().setPatch(deck.getPatch());
                                for (String cardId : deck.getCards().keySet()) {
                                    deck.getCards().get(cardId).setPatch(deck.getPatch());
                                }

                                emitter.onNext(
                                        new RxDatabaseEvent<Deck>(
                                                dataSnapshot.getKey(),
                                                deck,
                                                RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDeckQuery.addValueEventListener(mDeckDetailListener);
                    }
                });
            }
        });
    }

    @Override
    public void stopData() {
        if (mDecksQuery != null && mDecksListener != null) {
            mDecksQuery.removeEventListener(mDecksListener);
        }
        if (mDeckQuery != null && mDeckDetailListener != null) {
            mDeckQuery.removeEventListener(mDeckDetailListener);
        }
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> createNewDeck(String name, String faction, CardDetails leader, String patch) {
        String key = mUserReference.push().getKey();
        String author = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Deck deck = new Deck(key, name, faction, leader, author, patch);
        Map<String, Object> deckValues = deck.toMap();

        Map<String, Object> firebaseUpdates = new HashMap<>();
        firebaseUpdates.put(key, deckValues);

        mUserReference.updateChildren(firebaseUpdates);

        return getDeck(key, false);
    }

    @Override
    public void publishDeck(Deck deck) {
        String key = mPublicReference.child("decks").push().getKey();

        Map<String, Object> deckValues = deck.toMap();
        deckValues.put("id", key);
        deckValues.put("publicDeck", true);
        deckValues.put("week", 0);
        Map<String, Object> firebaseUpdates = new HashMap<>();
        firebaseUpdates.put(key, deckValues);

        mPublicReference.child("decks").updateChildren(firebaseUpdates);
    }

    @Override
    public void setLeader(Deck deck, final CardDetails leader) {
        DatabaseReference deckReference = mUserReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                storedDeck.setLeader(leader);

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
    public void renameDeck(Deck deck, final String newName) {
        DatabaseReference deckReference = mUserReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                storedDeck.setName(newName);

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
    public void addCardToDeck(Deck deck, final CardDetails card) {
        DatabaseReference deckReference = mUserReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                if (!storedDeck.canAddCard(card)) {
                    return Transaction.success(mutableData);
                }

                if (storedDeck.getCardCount().containsKey(card.getIngameId())) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedDeck.getCardCount().get(card.getIngameId());
                    storedDeck.getCardCount().put(card.getIngameId(), currentCardCount + 1);
                } else {
                    // Else add one card to the deck.
                    storedDeck.getCardCount().put(card.getIngameId(), 1);
                }

                storedDeck.getCards().put(card.getIngameId(), card);

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
    public void removeCardFromDeck(Deck deck, final CardDetails card) {
        DatabaseReference deckReference = mUserReference.child(deck.getId());

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Deck storedDeck = mutableData.getValue(Deck.class);
                if (storedDeck == null) {
                    // No deck with that id, this shouldn't occur.
                    return Transaction.success(mutableData);
                }

                if (storedDeck.getCardCount().containsKey(card.getIngameId())) {
                    // If the user already has at least one of these cards in their deck.
                    int currentCardCount = storedDeck.getCardCount().get(card.getIngameId());
                    storedDeck.getCardCount().put(card.getIngameId(), currentCardCount - 1);

                    if (currentCardCount == 0) {
                        storedDeck.getCards().put(card.getIngameId(), null);
                    }
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
