package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp3.ScrollView;
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel;
import com.jamieadkins.gwent.main.MvpPresenter;

import org.jetbrains.annotations.NotNull;

public interface CardDatabaseContract {
    interface View extends CardsContract.View, ScrollView {
        void showData(@NotNull CardDatabaseScreenModel data);

        void openUpdateScreen();
    }

    interface Presenter extends MvpPresenter {

        void search(@NotNull String query);

        void clearSearch();
    }
}
