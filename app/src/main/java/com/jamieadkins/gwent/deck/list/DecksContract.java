package com.jamieadkins.gwent.deck.list;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.commonutils.mvp2.BaseView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DecksContract {
    interface View extends BaseListView {

    }

    interface Presenter extends CardsContract.Presenter {
        void createNewDeck(String name, String faction, CardDetails leader, String patch);

        void publishDeck(Deck deck);

        void addCardToDeck(String deckId, CardDetails card);

        void removeCardFromDeck(String deckId, CardDetails card);

        void setLeader(Deck deck, CardDetails leader);

        void renameDeck(String deckId, String name);

        void deleteDeck(String deckId);
    }
}
