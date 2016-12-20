package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.card.CardFilter;
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

    private final String databasePath;

    public CardsInteractorFirebase() {
        databasePath = "card-data/v0-8-33";
        mCardsReference = mDatabase.getReference(databasePath);
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(final CardFilter filter) {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<CardDetails>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<CardDetails>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<CardDetails>> emitter) throws Exception {
                        Query cardsQuery = mCardsReference.orderByChild("name");

                        if (filter.getSearchQuery() != null) {
                            cardsQuery = cardsQuery.
                                    startAt(filter.getSearchQuery())
                                    // No 'contains' query so have to fudge it.
                                    .endAt(filter.getSearchQuery() + "zzzz");
                        }

                        ValueEventListener cardListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                                    CardDetails cardDetails = cardSnapshot.getValue(CardDetails.class);

                                    // Only add card if the card meets all the filters.
                                    if (filter.getFactions().get(cardDetails.getFaction()) &&
                                            filter.getRarities().get(cardDetails.getRarity()) &&
                                            filter.getTypes().get(cardDetails.getType())) {
                                        emitter.onNext(
                                                new RxDatabaseEvent<CardDetails>(
                                                        cardSnapshot.getKey(),
                                                        cardDetails,
                                                        RxDatabaseEvent.EventType.ADDED
                                                ));
                                    }
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
