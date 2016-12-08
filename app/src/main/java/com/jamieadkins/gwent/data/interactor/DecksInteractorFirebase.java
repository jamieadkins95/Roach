package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;

/**
 * Deals with firebase.
 */

public class DecksInteractorFirebase implements DecksInteractor {
    private DecksContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mDecksReference;
    private final DatabaseReference mDeckDetailsReference;

    public DecksInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDecksReference = mDatabase.getReference(userId + "/decks");
        mDeckDetailsReference = mDatabase.getReference(userId + "/deck-details");
    }

    @Override
    public void setPresenter(DecksPresenter presenter) {
        mPresenter = presenter;
    }

    private ChildEventListener mDecksListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mPresenter.sendDeckToView(dataSnapshot.getValue(Deck.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            mPresenter.onDeckRemoved(dataSnapshot.getValue(Deck.class).getId());
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
        mDecksReference.addChildEventListener(mDecksListener);
    }

    @Override
    public void createNewDeck(String name, String faction) {
        Deck deck = new Deck(name, faction);
        mDecksReference.child(deck.getId()).setValue(deck);
    }

    @Override
    public void stopData() {
        mDecksReference.removeEventListener(mDecksListener);
    }
}
