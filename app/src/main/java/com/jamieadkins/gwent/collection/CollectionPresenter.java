package com.jamieadkins.gwent.collection;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CollectionInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class CollectionPresenter implements CollectionContract.Presenter {
    private final CollectionInteractor mCollectionInteractor;
    private final CardsInteractor mCardsInteractor;
    private final CollectionContract.View mCollectionView;

    public CollectionPresenter(@NonNull CollectionContract.View collectionView,
                               @NonNull CollectionInteractor collectionInteractor,
                               @NonNull CardsInteractor cardsInteractor) {
        mCollectionInteractor = collectionInteractor;
        mCollectionInteractor.setPresenter(this);

        mCardsInteractor = cardsInteractor;
        mCardsInteractor.setPresenter(this);

        mCollectionView = collectionView;
        mCollectionView.setPresenter(this);
    }

    @Override
    public void stop() {
        mCollectionInteractor.stopCollectionUpdates();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter) {
        return mCardsInteractor.getCards(cardFilter);
    }

    @Override
    public Observable<Collection> getCollection() {
        return mCollectionInteractor.getCollection();
    }

    @Override
    public void addCard(String cardId) {
        mCollectionInteractor.addCardToCollection(cardId);
    }

    @Override
    public void removeCard(String cardId) {
        mCollectionInteractor.removeCardFromCollection(cardId);
    }

    @Override
    public void start() {

    }
}
