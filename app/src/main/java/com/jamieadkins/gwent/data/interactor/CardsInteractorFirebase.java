package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;

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

public class CardsInteractorFirebase implements CardsInteractor {
    private static final String LATEST_PATCH = "v0-8-60-2-images";

    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mCardsReference;

    private final String databasePath;
    private Query mCardsQuery;
    private ValueEventListener mCardListener;
    private String mPatch;

    private static Map<String, CardsInteractorFirebase> mInstances;

    public static CardsInteractorFirebase getInstance() {
        return getInstance(LATEST_PATCH);
    }

    public static CardsInteractorFirebase getInstance(String patch) {
        if (mInstances == null) {
            mInstances = new HashMap<>();
        }

        if (mInstances.keySet().contains(patch)) {
            return mInstances.get(patch);
        } else {
            mInstances.put(patch, new CardsInteractorFirebase(patch));
            return mInstances.get(patch);
        }
    }

    private CardsInteractorFirebase(String patch) {
        mPatch = patch;
        databasePath = "card-data/" + mPatch;
        mCardsReference = mDatabase.getReference(databasePath);
        // Keep Cards data in cache at all times.
        mCardsReference.keepSynced(true);
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(final CardFilter filter) {
        return Observable.defer(new Callable<ObservableSource<? extends RxDatabaseEvent<CardDetails>>>() {
            @Override
            public ObservableSource<? extends RxDatabaseEvent<CardDetails>> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<RxDatabaseEvent<CardDetails>> emitter) throws Exception {
                        mCardsQuery = mCardsReference.orderByChild("name");

                        if (filter.getSearchQuery() != null) {
                            mCardsQuery = mCardsQuery.
                                    startAt(filter.getSearchQuery())
                                    // No 'contains' query so have to fudge it.
                                    .endAt(filter.getSearchQuery() + "zzzz");
                        }

                        mCardListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                                    CardDetails cardDetails = cardSnapshot.getValue(CardDetails.class);

                                    if (cardDetails == null) {
                                        emitter.onError(new Throwable("Card doesn't exist."));
                                        emitter.onComplete();
                                        return;
                                    }

                                    cardDetails.setPatch(mPatch);

                                    // Only add card if the card meets all the filters.
                                    // Also check name and info are not null. Those are dud cards.
                                    if (filter.doesCardMeetFilter(cardDetails)) {
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

                        mCardsQuery.addListenerForSingleValueEvent(mCardListener);
                    }
                });
            }
        });
    }

    /**
     * We have a separate method for getting one card as it is significantly quicker than using a
     * card filter.
     * @param id id of the card to retrieve
     */
    @Override
    public Single<RxDatabaseEvent<CardDetails>> getCard(final String id) {
        return Single.defer(new Callable<SingleSource<? extends RxDatabaseEvent<CardDetails>>>() {
            @Override
            public SingleSource<? extends RxDatabaseEvent<CardDetails>> call() throws Exception {
                return Single.create(new SingleOnSubscribe<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void subscribe(final SingleEmitter<RxDatabaseEvent<CardDetails>> emitter) throws Exception {
                        mCardsQuery = mCardsReference.child(id);

                        mCardListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                CardDetails cardDetails = dataSnapshot.getValue(CardDetails.class);

                                if (cardDetails == null) {
                                    emitter.onError(new Throwable("Card doesn't exist."));
                                    return;
                                }

                                cardDetails.setPatch(mPatch);

                                emitter.onSuccess(
                                        new RxDatabaseEvent<CardDetails>(
                                                dataSnapshot.getKey(),
                                                cardDetails,
                                                RxDatabaseEvent.EventType.ADDED
                                        ));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        mCardsQuery.addListenerForSingleValueEvent(mCardListener);
                    }
                });
            }
        });
    }

    @Override
    public void removeListeners() {
        if (mCardsQuery != null) {
            mCardsQuery.removeEventListener(mCardListener);
        }
    }
}
