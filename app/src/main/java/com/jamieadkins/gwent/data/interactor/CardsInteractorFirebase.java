package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

/**
 * Deals with firebase.
 */

public class CardsInteractorFirebase implements CardsInteractor {
    private CardsContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mCardsReference;

    private static final String[] START_POINTS = new String[] {"A", "E", "M", "S", "ZZ"};
    private int currentStartAtIndex = 0;

    private final String databasePath;

    public CardsInteractorFirebase() {
        databasePath = "card-data/test";
        mCardsReference = mDatabase.getReference(databasePath);
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void resetMorePagesCounter() {
        currentStartAtIndex = 0;
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getMoreCards() {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<CardDetails>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<CardDetails>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<CardDetails>> emitter) throws Exception {
                        if (currentStartAtIndex == START_POINTS.length - 1) {
                            emitter.onComplete();
                            return;
                        }

                        String startAt = START_POINTS[currentStartAtIndex];
                        String endAt = START_POINTS[currentStartAtIndex + 1];
                        currentStartAtIndex++;

                        Query cardsQuery = mCardsReference.orderByChild("name")
                                .startAt(startAt)
                                .endAt(endAt);

                        ValueEventListener cardListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                                    emitter.onNext(
                                            new RxDatabaseEvent<CardDetails>(
                                                    cardSnapshot.getKey(),
                                                    cardSnapshot.getValue(CardDetails.class),
                                                    RxDatabaseEvent.EventType.ADDED
                                            ));
                                }

                                emitter.onComplete();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        cardsQuery.addListenerForSingleValueEvent(cardListener);
                    }
                });
            }
        });
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> search(final String query) {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<CardDetails>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<CardDetails>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<CardDetails>> emitter) throws Exception {
                        // Sort alphabetically the cards
                        Query cardsQuery = mCardsReference.orderByChild("name")
                                .startAt(query)
                                // There is no 'contains' query, so we have to fudge it like this.
                                .endAt(query + "zzzz");

                        ValueEventListener cardListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                                    emitter.onNext(
                                            new RxDatabaseEvent<CardDetails>(
                                                    cardSnapshot.getKey(),
                                                    cardSnapshot.getValue(CardDetails.class),
                                                    RxDatabaseEvent.EventType.ADDED
                                            ));
                                }

                                emitter.onComplete();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        cardsQuery.addListenerForSingleValueEvent(cardListener);
                    }
                });
            }
        });
    }

    @Override
    public CardDetails getCard(String id) {
        return null;
    }
}
