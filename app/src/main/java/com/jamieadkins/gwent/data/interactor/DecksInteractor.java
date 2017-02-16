package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Observable;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor<DecksContract.Presenter> {

    void createNewDeck(String name, String faction, CardDetails leader, String patch);

    void addCardToDeck(Deck deck, CardDetails card);

    void removeCardFromDeck(Deck deck, CardDetails card);

    void publishDeck(Deck deck);

    Observable<RxDatabaseEvent<Deck>> getDecks();

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId);

    void stopData();
}
