package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.model.GwentCard;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardDatabaseContract {
    interface View extends CardsContract.View {
        void showUpdateAvailable();
    }

    interface Presenter {
        // Nothing here.
    }
}
