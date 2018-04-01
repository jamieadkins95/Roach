package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.core.GwentCard;
import com.jamieadkins.gwent.filter.FilterableItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseListView {
        void showCards(@NotNull List<GwentCard> cards);

        void showFilterMenu(@NotNull List<FilterableItem> filters);
    }

    interface Presenter {
        // Nothing here.
    }
}
