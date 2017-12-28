package com.jamieadkins.gwent.card.detail;

import android.content.Context;

import com.jamieadkins.commonutils.mvp2.BaseView;
import com.jamieadkins.gwent.data.card.CardDetails;
import com.jamieadkins.gwent.model.GwentCard;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DetailContract {
    interface View extends BaseView {

        void setLoadingIndicator(boolean active);

        Context getContext();

        void showCard(GwentCard card);
    }

    interface Presenter {

    }
}
