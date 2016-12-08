package com.jamieadkins.gwent.data;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.jgaw.Faction;

/**
 * Deals with firebase.
 */

public class DecksInteractor {
    private final DecksContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mDecksReference;

    public DecksInteractor(DecksContract.Presenter presenter, String userId) {
        mPresenter = presenter;
        mDecksReference = mDatabase.getReference(userId + "/decks");
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

    public void requestDecks() {
        mDecksReference.addChildEventListener(mDecksListener);
    }

    public void createNewDeck() {
        Deck deck = new Deck(Faction.SKELLIGE);
        mDecksReference.child(deck.getId()).setValue(deck);
    }

    public void stopData() {
        mDecksReference.removeEventListener(mDecksListener);
    }
}
