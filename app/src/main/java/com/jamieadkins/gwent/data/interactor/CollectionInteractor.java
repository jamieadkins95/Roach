package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.collection.CollectionContract;
import com.jamieadkins.gwent.data.Collection;

import io.reactivex.Observable;

/**
 * Collection manipulation class.
 */

public interface CollectionInteractor extends BaseInteractor<CollectionContract.Presenter> {

    void addCardToCollection(String cardId, String variationId);

    void removeCardFromCollection(String cardId, String variationId);

    Observable<Collection> getCollection();

    void stopCollectionUpdates();
}
