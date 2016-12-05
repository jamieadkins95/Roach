package com.jamieadkins.jgaw;

/**
 * Created by jamiea on 05/12/16.
 */

public interface CardResultListener {
    void onCardRetrieved(Card card);
    void onError(Throwable error);
}
