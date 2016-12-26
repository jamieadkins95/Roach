package com.jamieadkins.gwent.deck;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksInteractor decksInteractor) {
        mDecksInteractor = decksInteractor;
        mDecksInteractor.setPresenter(this);
    }

    public DecksPresenter() {
        mDecksInteractor = new DecksInteractorFirebase();
        mDecksInteractor.setPresenter(this);
    }

    @Override
    public void createNewDeck(String name, String faction) {
        mDecksInteractor.createNewDeck(name, faction);
    }

    @Override
    public void start() {
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public void bindView(DecksContract.View view) {
        mDecksView = view;
    }

    @Override
    public void unbindView() {
        mDecksView = null;
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDecks() {
        return mDecksInteractor.getDecks();
    }

    @Override
    public void stop() {
        mDecksInteractor.stopData();
    }

    @Override
    public void onLoadingComplete() {
        mDecksView.setLoadingIndicator(false);
    }
}
