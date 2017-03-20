package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor<DecksContract.Presenter> {

    Observable<RxDatabaseEvent<Deck>> createNewDeck(String name, String faction, CardDetails leader, String patch);

    void addCardToDeck(Deck deck, CardDetails card);

    void setLeader(Deck deck, CardDetails leader);

    void renameDeck(Deck deck, String newName);

    void removeCardFromDeck(Deck deck, CardDetails card);

    void publishDeck(Deck deck);

    void deleteDeck(Deck deck);

    Observable<RxDatabaseEvent<Deck>> getUserDecks();

    Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek();

    Observable<RxDatabaseEvent<Deck>> getFeaturedDecks();

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck);

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck, boolean evaluate);

    void stopData();

    void upgradeDeckToPatch(String deckId, String patch);
}
