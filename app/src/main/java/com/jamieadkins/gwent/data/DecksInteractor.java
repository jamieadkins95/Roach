package com.jamieadkins.gwent.data;

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

    public void requestDecks() {
        mDecksReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPresenter.sendDeckToView(dataSnapshot.getValue(Deck.class));
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
        });
    }

    public void createNewDeck() {
        mDecksReference.child(String.valueOf(System.currentTimeMillis())).setValue(new Deck(Faction.SKELLIGE));
    }
}
