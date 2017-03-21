package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.base.BaseCompletableObserver;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.deck.list.DecksContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;

import static com.jamieadkins.gwent.data.Deck.MAX_CARD_COUNT;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_BRONZE;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_GOLD;
import static com.jamieadkins.gwent.data.Deck.MAX_EACH_SILVER;

/**
 * Deals with firebase.
 */

public class DecksInteractorFirebase implements DecksInteractor {
    private static final String PUBLIC_DECKS_PATH = "public-decks/";

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Query mDecksQuery;
    private Query mDeckQuery;
    private Query mCardCountQuery;
    private final DatabaseReference mPublicReference;
    private DatabaseReference mUserReference;

    private CardsInteractor mCardsInteractor;

    private List<ChildEventListener> mDeckListListeners = new ArrayList<>();
    private List<ValueEventListener> mDeckDetailListeners = new ArrayList<>();
    private List<ChildEventListener> mCardCountListeners = new ArrayList<>();

    public DecksInteractorFirebase() {
        mPublicReference = mDatabase.getReference(PUBLIC_DECKS_PATH);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mUserReference = mDatabase.getReference("users/" + userId + "/decks/");
            mUserReference.keepSynced(true);
        }
    }

    private Deck checkLegacy(DataSnapshot deckSnapshot) {
        Deck deck = deckSnapshot.getValue(Deck.class);

        if (!deckSnapshot.hasChild("leaderId") && deckSnapshot.hasChild("leader")) {
            deck.setLeaderId(deckSnapshot.child("leader").child("ingameId").getValue(String.class));
        }

        return deck;
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

                        ChildEventListener listener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(CardsInteractorFirebase.getInstance())
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        RxDatabaseEvent.EventType eventType = RxDatabaseEvent.EventType.ADDED;
                                                        if (deck.isDeleted()) {
                                                            eventType = RxDatabaseEvent.EventType.REMOVED;
                                                        }

                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        eventType));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildChanged(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(CardsInteractorFirebase.getInstance())
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        RxDatabaseEvent.EventType eventType = RxDatabaseEvent.EventType.CHANGED;
                                                        if (deck.isDeleted()) {
                                                            eventType = RxDatabaseEvent.EventType.REMOVED;
                                                        }

                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        eventType));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot deckSnapshot) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(CardsInteractorFirebase.getInstance())
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        RxDatabaseEvent.EventType.REMOVED));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onChildMoved(DataSnapshot deckSnapshot, String s) {
                                final Deck deck = checkLegacy(deckSnapshot);

                                deck.evaluateDeck(CardsInteractorFirebase.getInstance())
                                        .subscribe(
                                                new BaseCompletableObserver() {
                                                    @Override
                                                    public void onComplete() {
                                                        emitter.onNext(
                                                                new RxDatabaseEvent<Deck>(
                                                                        deck.getId(),
                                                                        deck,
                                                                        RxDatabaseEvent.EventType.MOVED));
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDecksQuery.addChildEventListener(listener);
                        mDeckListListeners.add(listener);
                        mDecksQuery.addListenerForSingleValueEvent(initialCountListener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Integer>> subscribeToCardCountUpdates(String deckId) {
        mCardCountQuery = mUserReference.child(deckId).child("cardCount");
        mCardCountQuery.keepSynced(true);
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<Integer>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<Integer>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<Integer>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<Integer>> emitter) throws Exception {
                        ValueEventListener initialCountListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emitter.onNext(RxDatabaseEvent.INITIAL_LOAD_COMPLETE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        ChildEventListener listener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                int count = dataSnapshot.getValue(Integer.class);
                                emitter.onNext(new RxDatabaseEvent<Integer>(
                                        dataSnapshot.getKey(),
                                        count,
                                        RxDatabaseEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mCardCountQuery.addChildEventListener(listener);
                        mCardCountListeners.add(listener);
                        mCardCountQuery.addListenerForSingleValueEvent(initialCountListener);
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
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Deck deck = checkLegacy(dataSnapshot);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    return;
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

                        mDeckQuery.addListenerForSingleValueEvent(listener);
                        mDeckDetailListeners.add(listener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(final String deckId, boolean isPublicDeck) {
        // Return unevaluated deck by default.
        return getDeck(deckId, isPublicDeck, false);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(final String deckId, boolean isPublicDeck,
                                                     final boolean evaluate) {
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
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Deck deck = checkLegacy(dataSnapshot);

                                if (deck == null) {
                                    emitter.onError(new Throwable("Deck doesn't exist"));
                                    emitter.onComplete();
                                    return;
                                }

                                if (evaluate) {
                                    deck.evaluateDeck(CardsInteractorFirebase.getInstance())
                                            .subscribe(
                                                    new BaseCompletableObserver() {
                                                        @Override
                                                        public void onComplete() {
                                                            emitter.onNext(
                                                                    new RxDatabaseEvent<Deck>(
                                                                            deck.getId(),
                                                                            deck,
                                                                            RxDatabaseEvent.EventType.ADDED));
                                                        }
                                                    }
                                            );
                                } else {
                                    emitter.onNext(
                                            new RxDatabaseEvent<Deck>(
                                                    deck.getId(),
                                                    deck,
                                                    RxDatabaseEvent.EventType.ADDED));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mDeckQuery.addValueEventListener(listener);
                        mDeckDetailListeners.add(listener);
                    }
                });
            }
        });
    }

    @Override
    public void stopData() {
        if (mDecksQuery != null && mDeckListListeners != null) {
            for (ChildEventListener listener : mDeckListListeners) {
                mDecksQuery.removeEventListener(listener);
            }
            mDeckListListeners = null;
        }
        if (mDeckQuery != null && mDeckDetailListeners != null) {
            for (ValueEventListener listener : mDeckDetailListeners) {
                mDeckQuery.removeEventListener(listener);
            }
            mDeckDetailListeners = null;
        }
        if (mCardCountQuery != null && mCardCountListeners != null) {
            for (ChildEventListener listener : mCardCountListeners) {
                mCardCountQuery.removeEventListener(listener);
            }
            mCardCountListeners = null;
        }
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> createNewDeck(String name, String faction, CardDetails leader, String patch) {
        String key = mUserReference.push().getKey();
        String author = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Deck deck = new Deck(key, name, faction, leader.getIngameId(), author, patch);
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
        DatabaseReference deckReference = mUserReference.child(deck.getId()).child("leaderId");

        // Transactions will ensure concurrency errors don't occur.
        deckReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // Set value and report transaction success.
                mutableData.setValue(leader.getIngameId());
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
    public Completable renameDeck(final String deckId, final String newName) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        DatabaseReference deckReference = mUserReference.child(deckId).child("name");

                        // Transactions will ensure concurrency errors don't occur.
                        deckReference.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                // Set value and report transaction success.
                                mutableData.setValue(newName);
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
                });
            }
        });
    }

    @Override
    public Completable deleteDeck(final String deckId) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        DatabaseReference deckReference = mUserReference.child(deckId).child("deleted");

                        // Transactions will ensure concurrency errors don't occur.
                        deckReference.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                // Set value and report transaction success.
                                mutableData.setValue(true);
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
                });
            }
        });
    }

    @Override
    public Completable addCardToDeck(final String deckId, final CardDetails card) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        DatabaseReference deckReference = mUserReference.child(deckId).child("cardCount");

                        // Transactions will ensure concurrency errors don't occur.
                        deckReference.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Map<String, Long> cards = (Map<String, Long>) mutableData.getValue();

                                if (cards == null) {
                                    cards = new HashMap<String, Long>();
                                }

                                if (!canAddCard(cards, card)) {
                                    return Transaction.success(mutableData);
                                }

                                if (cards.containsKey(card.getIngameId())) {
                                    // If the user already has at least one of these cards in their deck.
                                    long currentCardCount = cards.get(card.getIngameId());
                                    cards.put(card.getIngameId(), currentCardCount + 1);
                                } else {
                                    // Else add one card to the deck.
                                    cards.put(card.getIngameId(), 1L);
                                }

                                // Set value and report transaction success.
                                mutableData.setValue(cards);
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
                });
            }
        });
    }

    public boolean canAddCard(Map<String, Long> cards, CardDetails cardDetails) {
        int count = 0;
        for (String cardId : cards.keySet()) {
            count += cards.get(cardId);
        }

        if (count >= MAX_CARD_COUNT) {
            return false;
        }

        if (cards.containsKey(cardDetails.getIngameId())) {
            // If the user already has at least one of these cards in their deck.
            long currentCardCount = cards.get(cardDetails.getIngameId());
            switch (cardDetails.getType()) {
                case Type.BRONZE_ID:
                    return currentCardCount < MAX_EACH_BRONZE;
                case Type.SILVER_ID:
                    return currentCardCount < MAX_EACH_SILVER;
                case Type.GOLD_ID:
                    return currentCardCount < MAX_EACH_GOLD;
                default:
                    return false;
            }
        } else {
            // Deck doesn't contain this card yet, can add as long as the card isn't a leader card.
            return !cardDetails.getType().equals(Type.LEADER_ID);
        }

    }

    @Override
    public void upgradeDeckToPatch(String deckId, final String patch) {
        final DatabaseReference patchReference = mUserReference.child(deckId).child("patch");

        patchReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData mutableData) {
                mutableData.setValue(patch);
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
    public Completable removeCardFromDeck(final String deckId, final CardDetails card) {
        return Completable.defer(new Callable<CompletableSource>() {
            @Override
            public CompletableSource call() throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {

                        DatabaseReference deckReference = mUserReference.child(deckId).child("cardCount");

                        // Transactions will ensure concurrency errors don't occur.
                        deckReference.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Map<String, Long> cards = (Map<String, Long>) mutableData.getValue();

                                if (cards.containsKey(card.getIngameId())) {
                                    // If the user already has at least one of these cards in their deck.
                                    long currentCardCount = cards.get(card.getIngameId());
                                    long newCount = currentCardCount - 1;

                                    if (newCount == 0) {
                                        cards.remove(card.getIngameId());
                                    } else {
                                        cards.put(card.getIngameId(), newCount);
                                    }
                                } else {
                                    // This deck doesn't have that card in it.
                                }

                                // Set value and report transaction success.
                                mutableData.setValue(cards);
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
                });
            }
        });
    }
}
