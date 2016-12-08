package com.jamieadkins.gwent.data.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.data.Card;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Deals with firebase.
 */

public class DecksInteractorFirebase implements DecksInteractor {
    private DecksContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mDecksReference;

    private final String databasePath;

    public DecksInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePath = userId + "/decks/";
        mDecksReference = mDatabase.getReference(databasePath);
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
        String key = mDecksReference.push().getKey();
        Deck deck = new Deck(key, name, faction);
        Map<String, Object> deckValues = deck.toMap();

        Map<String, Object> firebaseUpdates = new HashMap<>();
        firebaseUpdates.put(key, deckValues);

        mDecksReference.updateChildren(firebaseUpdates);
    }

    @Override
    public void addCardToDeck(Deck deck, Card card) {
        mDecksReference.child(deck.getId()).child("cards").child(card.getCardId()).setValue(card);
    }

    @Override
    public void removeCardFromDeck(Deck deck, Card card) {
        mDecksReference.child(deck.getId()).child("cards").child(card.getCardId()).removeValue();
    }

    @Override
    public void stopData() {
        mDecksReference.removeEventListener(mDecksListener);
    }
}
