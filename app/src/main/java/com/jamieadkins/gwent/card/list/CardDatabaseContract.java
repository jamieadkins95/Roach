package com.jamieadkins.gwent.card.list;

import com.jamieadkins.gwent.core.GwentCard;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardDatabaseContract {
    interface View extends CardsContract.View {
        void showUpdateAvailable();

        void showCards2(@NotNull Collection<GwentCard> cards);
    }

    interface Presenter {
        // Nothing here.
    }
}
