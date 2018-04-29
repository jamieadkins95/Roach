package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp3.ScrollView;

import org.jetbrains.annotations.NotNull;

public interface CardDatabaseContract {
    interface View extends CardsContract.View, ScrollView {
        void showUpdateAvailable();
    }

    interface Presenter {

        void search(@NotNull String query);

        void clearSearch();
    }
}
