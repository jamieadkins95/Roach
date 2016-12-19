package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * Card manipulation class.
 */

public interface CardsInteractor extends BaseInteractor<CardsContract.Presenter> {

    CardDetails getCard(String id);
}
