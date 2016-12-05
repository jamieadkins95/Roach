package com.jamieadkins.jgaw;

import com.jamieadkins.jgaw.Card.Card;

/**
 * Created by jamiea on 05/12/16.
 */

public interface CardResultListener {
    void onCardRetrieved(Card card);
    void onError(Throwable error);
}
