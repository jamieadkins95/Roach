package com.jamieadkins.gwent.collection;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.interactor.CollectionInteractor;

import io.reactivex.Observable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class CollectionPresenter implements CollectionContract.Presenter {
    private final CollectionInteractor mCollectionInteractor;
    private final CollectionContract.View mCollectionView;

    public CollectionPresenter(@NonNull CollectionContract.View collectionView,
                               @NonNull CollectionInteractor collectionInteractor) {
        mCollectionInteractor = collectionInteractor;
        mCollectionInteractor.setPresenter(this);

        mCollectionView = collectionView;
        mCollectionView.setPresenter(this);
    }

    @Override
    public void stop() {

    }

    @Override
    public Observable<Collection> getCollection() {
        return null;
    }

    @Override
    public void start() {

    }
}
