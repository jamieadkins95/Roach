package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.core.CardDatabaseResult;

import org.jetbrains.annotations.NotNull;

public interface CardsContract {
    interface View extends BaseListView {
        void showCards(@NotNull CardDatabaseResult result);

        void showCardDetails(@NotNull String cardId);
    }

    interface Presenter {
        // Nothing here.
    }
}
