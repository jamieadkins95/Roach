package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * Deals with firebase.
 */

public class CardsInteractorFirebase implements CardsInteractor {
    private CardsContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mCardsReference;

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
    }

    @Override
    public CardDetails getCard(String id) {
        return null;
    }
}
