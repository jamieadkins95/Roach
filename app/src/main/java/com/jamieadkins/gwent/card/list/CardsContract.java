package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.core.CardDatabaseResult;
import com.jamieadkins.gwent.core.GwentCard;
import com.jamieadkins.gwent.filter.FilterableItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CardsContract {
    interface View extends BaseListView {
        void showCards(@NotNull CardDatabaseResult result);
    }

    interface Presenter {
        // Nothing here.
    }
}
