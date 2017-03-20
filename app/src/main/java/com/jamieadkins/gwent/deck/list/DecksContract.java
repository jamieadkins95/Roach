package com.jamieadkins.gwent.deck.list;

import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DecksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends CardsContract.Presenter {
        Observable<RxDatabaseEvent<Deck>> getUserDecks();

        Observable<RxDatabaseEvent<Deck>> getPublicDecks();

        Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck);

        Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck, boolean evaluate);

        Observable<RxDatabaseEvent<CardDetails>> getLeadersForFaction(String factionId);

        Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek();

        void stop();

        Observable<RxDatabaseEvent<Deck>> createNewDeck(String name, String faction, CardDetails leader, String patch);

        void publishDeck(Deck deck);

        void onLoadingComplete();

        void addCardToDeck(Deck deck, CardDetails card);

        void removeCardFromDeck(Deck deck, CardDetails card);

        void setLeader(Deck deck, CardDetails leader);

        void renameDeck(Deck deck, String name);

        void deleteDeck(Deck deck);

        Single<String> getLatestPatch();

        void upgradeDeckToPatch(String deckId, String newPatch);
    }
}
