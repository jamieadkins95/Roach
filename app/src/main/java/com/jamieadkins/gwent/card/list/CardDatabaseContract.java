package com.jamieadkins.gwent.card.list;

import javax.annotation.Nullable;

public interface CardDatabaseContract {
    interface View extends CardsContract.View {
        void showUpdateAvailable();
    }

    interface Presenter {

        void search(@Nullable String query);

        void clearSearch();
    }
}
