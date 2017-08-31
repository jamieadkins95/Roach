package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Deck manipulation class.
 */

public interface DecksInteractor extends BaseInteractor {

    String createNewDeck(String name, String faction);

    Completable addCardToDeck(String deckId, CardDetails card);

    void setLeader(Deck deck, CardDetails leader);

    Completable renameDeck(String deckId, String newName);

    Completable removeCardFromDeck(String deckId, CardDetails card);

    void publishDeck(Deck deck);

    Completable deleteDeck(String deckId);

    Observable<RxDatabaseEvent<Deck>> getUserDecks();

    Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek();

    Observable<RxDatabaseEvent<Deck>> getFeaturedDecks();

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck);

    Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck, boolean evaluate);

    Observable<RxDatabaseEvent<Integer>> subscribeToCardCountUpdates(String deckId);

    void stopData();
}
