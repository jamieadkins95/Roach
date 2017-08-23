package com.jamieadkins.gwent.collection;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CollectionContract {
    interface View extends CardsContract.View {
        void showCollection(String cardId, Map<String, Long> collection);
    }

    interface Presenter extends CardsContract.Presenter {
        // Nothing extra here.
    }
}
