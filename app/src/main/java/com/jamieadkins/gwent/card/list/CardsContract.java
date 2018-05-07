package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;

import org.jetbrains.annotations.NotNull;

public interface CardsContract {
    interface View extends BaseListView {

        void showCardDetails(@NotNull String cardId);
    }

    interface Presenter {
        // Nothing here.
    }
}
