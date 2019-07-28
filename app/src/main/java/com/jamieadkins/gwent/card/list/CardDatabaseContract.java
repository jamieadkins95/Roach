package com.jamieadkins.gwent.card.list;

import com.jamieadkins.gwent.base.ScrollView;
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel;
import com.jamieadkins.gwent.base.MvpPresenter;

import org.jetbrains.annotations.NotNull;

public interface CardDatabaseContract {
    interface View extends CardsContract.View, ScrollView {
        void showData(@NotNull CardDatabaseScreenModel data);

        void openUpdateScreen();

        void showLoadingIndicator(Boolean loading);
    }

    interface Presenter extends MvpPresenter {

        void search(@NotNull String query);

        void clearSearch();
    }
}
