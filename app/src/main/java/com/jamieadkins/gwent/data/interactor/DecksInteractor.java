package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Observable;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor<DecksContract.Presenter> {

    void createNewDeck(String name, String faction);

    void addCardToDeck(Deck deck, String cardId, String variationId);

    void removeCardFromDeck(Deck deck, String cardId, String variationId);

    Observable<RxDatabaseEvent<Deck>> getDecks();

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId);

    void stopData();
}
