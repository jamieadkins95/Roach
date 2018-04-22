package com.jamieadkins.gwent.card.list;

import org.jetbrains.annotations.NotNull;

public interface CardDatabaseContract {
    interface View extends CardsContract.View {
        void showUpdateAvailable();
    }

    interface Presenter {

        void search(@NotNull String query);

        void clearSearch();
    }
}
