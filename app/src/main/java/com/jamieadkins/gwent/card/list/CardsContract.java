package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.core.GwentCard;
import com.jamieadkins.gwent.filter.FilterableItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CardsContract {
    interface View extends BaseListView {
        void showCards(@NotNull List<GwentCard> cards);

        void showSearchResults(@NotNull String query, @NotNull List<GwentCard> cards);
    }

    interface Presenter {
        // Nothing here.
    }
}
