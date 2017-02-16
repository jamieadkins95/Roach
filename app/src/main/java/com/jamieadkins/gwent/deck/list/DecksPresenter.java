package com.jamieadkins.gwent.deck.list;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final PatchInteractor mPatchInteractor;
    private final CardsInteractor mCardsInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView,
                          @NonNull DecksInteractor decksInteractor) {
        mDecksInteractor = decksInteractor;
        mDecksInteractor.setPresenter(this);

        mPatchInteractor = new PatchInteractorFirebase();
        mCardsInteractor = new CardsInteractorFirebase();

        mDecksView = decksView;
        mDecksView.setPresenter(this);
    }

    @Override
    public void createNewDeck(final String name, final String faction, final CardDetails leader) {
        mPatchInteractor.getLatestPatch().subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String patch) {
                mDecksInteractor.createNewDeck(name, faction, leader, patch);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void start() {
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDecks() {
        return mDecksInteractor.getDecks();
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(String deckId) {
        return mDecksInteractor.getDeck(deckId);
    }

    @Override
    public void publishDeck(Deck deck) {
        mDecksInteractor.publishDeck(deck);
    }

    @Override
    public void stop() {
        mDecksInteractor.stopData();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter) {
        return mCardsInteractor.getCards(cardFilter);
    }

    @Override
    public void onLoadingComplete() {
        mDecksView.setLoadingIndicator(false);
    }
}
