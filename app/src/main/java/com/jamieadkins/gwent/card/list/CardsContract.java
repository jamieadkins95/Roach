package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.model.GwentCard;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseListView {
        void showCards(@NotNull List<GwentCard> cards);

        void showNewPatch(@NotNull String patchName);
    }

    interface Presenter {
        // Nothing here.
    }
}
