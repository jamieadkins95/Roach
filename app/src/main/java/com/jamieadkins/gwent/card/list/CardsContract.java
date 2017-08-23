package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseListView {
        void showIntelligentSearchFailure();

        void showAlgoliaAttribution();

        void hideAlgoliaAttribution();
    }

    interface Presenter {
        // Nothing here.
    }
}
