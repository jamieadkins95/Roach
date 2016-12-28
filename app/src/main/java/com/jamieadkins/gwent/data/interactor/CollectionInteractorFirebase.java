package com.jamieadkins.gwent.data.interactor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.jamieadkins.gwent.collection.CollectionContract;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.deck.DecksContract;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;

/**
 * Deals with firebase.
 */

public class CollectionInteractorFirebase implements CollectionInteractor {
    private CollectionContract.Presenter mPresenter;
    private final FirebaseDatabase mDatabase = FirebaseUtils.getDatabase();
    private final DatabaseReference mCollectionReference;

    private final String databasePath;

    public CollectionInteractorFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databasePath = "users/" + userId + "/collection/";
        mCollectionReference = mDatabase.getReference(databasePath);
    }

    @Override
    public void setPresenter(CollectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addCardToCollection(String cardId) {

    }

    @Override
    public void removeCardFromCollection(String cardId) {

    }

    @Override
    public Observable<Collection> getCollection() {
        return null;
    }
}
