package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.twitter.sdk.android.core.models.Card;

import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    private ChildEventListener mCardsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mPresenter.sendCardToView(dataSnapshot.getValue(CardDetails.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void requestData() {
        mCardsReference.addChildEventListener(mCardsListener);
    }

    @Override
    public void stopData() {
        mCardsReference.removeEventListener(mCardsListener);
        currentStartAtIndex = 0;
    }

    @Override
    public Observable<CardDetails> getMoreCards() {
        return Observable.defer(new Callable<ObservableSource<? extends CardDetails>>() {
            @Override
            public ObservableSource<? extends CardDetails> call() throws Exception {
                return Observable.create(new ObservableOnSubscribe<CardDetails>() {
                    @Override
                    public void subscribe(final ObservableEmitter<CardDetails> emitter) throws Exception {
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
                                int count = 0;
                                for (DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                                    emitter.onNext(cardSnapshot.getValue(CardDetails.class));
                                    count++;
                                }
                                Log.d("JAMIEA", count + "");

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
