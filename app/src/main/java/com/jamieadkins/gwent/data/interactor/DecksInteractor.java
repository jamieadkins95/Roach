package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor<DecksContract.Presenter> {

    void createNewDeck(String name, String faction);

    void addCardToDeck(Deck deck, String cardId);

    void removeCardFromDeck(Deck deck, String cardId);
}
