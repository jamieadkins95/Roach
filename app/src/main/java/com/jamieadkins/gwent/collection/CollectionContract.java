package com.jamieadkins.gwent.collection;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.Collection;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CollectionContract {
    interface View extends CardsContract.View {
        // Don't need any additional methods.
    }

    interface Presenter extends CardsContract.Presenter {
        Observable<Collection> getCollection();

        void addCard(String cardId, String variationId);

        void removeCard(String cardId, String variationId);
    }
}
