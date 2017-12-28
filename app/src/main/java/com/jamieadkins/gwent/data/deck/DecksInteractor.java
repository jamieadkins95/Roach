package com.jamieadkins.gwent.data.deck;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.model.GwentFaction;
import com.jamieadkins.gwent.model.GwentCard;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor {

    String createNewDeck(String name, GwentFaction faction);

    void addCardToDeck(String deckId, GwentCard card);

    void setLeader(String deckId, String leaderId);

    void renameDeck(String deckId, String newName);

    void removeCardFromDeck(String deckId, GwentCard card);

    void publishDeck(String deckId);

    void deleteDeck(String deckId);

    Observable<RxDatabaseEvent<Deck>> getUserDecks();

    Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek();

    Observable<RxDatabaseEvent<Deck>> getFeaturedDecks();

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck);

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck, boolean evaluate);

    Observable<RxDatabaseEvent<Integer>> subscribeToCardCountUpdates(String deckId);

    void stopData();
}
