package com.jamieadkins.gwent.card.list;

import org.jetbrains.annotations.NotNull;

public interface CardsContract {
    interface View {

        void showCardDetails(@NotNull String cardId);
    }

    interface Presenter {
        // Nothing here.
    }
}
