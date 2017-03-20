package com.jamieadkins.gwent.deck.list;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final PatchInteractor mPatchInteractor;
    private final CardsInteractor mCardsInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView,
                          @NonNull DecksInteractor decksInteractor,
                          @NonNull CardsInteractor cardsInteractor,
                          @NonNull PatchInteractor patchInteractor) {
        mDecksInteractor = decksInteractor;

        mPatchInteractor = patchInteractor;

        mCardsInteractor = cardsInteractor;

        mDecksView = decksView;
        mDecksView.setPresenter(this);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> createNewDeck(final String name, final String faction,
                                                           final CardDetails leader, String patch) {
        return mDecksInteractor.createNewDeck(name, faction, leader, patch);
    }

    @Override
    public void start() {
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getUserDecks() {
        return mDecksInteractor.getUserDecks();
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getPublicDecks() {
        return mDecksInteractor.getFeaturedDecks();
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck) {
        return mDecksInteractor.getDeck(deckId, isPublicDeck);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck, boolean evaluate) {
        return mDecksInteractor.getDeck(deckId, isPublicDeck, evaluate);
    }

    @Override
    public Observable<RxDatabaseEvent<Integer>> subscribeToCardCountUpdates(String deckId) {
        return mDecksInteractor.subscribeToCardCountUpdates(deckId);
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek() {
        return mDecksInteractor.getDeckOfTheWeek();
    }

    @Override
    public void publishDeck(Deck deck) {
        mDecksInteractor.publishDeck(deck);
    }

    @Override
    public void deleteDeck(Deck deck) {
        mDecksInteractor.deleteDeck(deck);
    }

    @Override
    public void stop() {
        mDecksInteractor.stopData();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter) {
        return mCardsInteractor.getCards(cardFilter);
    }

    @Override
    public Single<RxDatabaseEvent<CardDetails>> getCard(String cardId) {
        return mCardsInteractor.getCard(cardId);
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getLeadersForFaction(String factionId) {
        CardFilter cardFilter = new CardFilter();

        // Set filter to leaders of this faction only.
        for (Filterable filterable : Faction.ALL_FACTIONS) {
            if (!filterable.getId().equals(factionId)) {
                cardFilter.put(filterable.getId(), false);
            }
        }
        cardFilter.put(Type.BRONZE_ID, false);
        cardFilter.put(Type.SILVER_ID, false);
        cardFilter.put(Type.GOLD_ID, false);

        return mCardsInteractor.getCards(cardFilter);
    }

    @Override
    public void addCardToDeck(String deckId, CardDetails card) {
        mDecksInteractor.addCardToDeck(deckId, card);
    }

    @Override
    public void removeCardFromDeck(String deckId, CardDetails card) {
        mDecksInteractor.removeCardFromDeck(deckId, card);
    }

    @Override
    public Completable renameDeck(String deckId, String name) {
        return mDecksInteractor.renameDeck(deckId, name);
    }

    @Override
    public void setLeader(Deck deck, CardDetails leader) {
        mDecksInteractor.setLeader(deck, leader);
    }

    @Override
    public void onLoadingComplete() {
        mDecksView.setLoadingIndicator(false);
    }

    @Override
    public Single<String> getLatestPatch() {
        return mPatchInteractor.getLatestPatch();
    }

    @Override
    public void upgradeDeckToPatch(String deckId, String newPatch) {
        mDecksInteractor.upgradeDeckToPatch(deckId, newPatch);
    }
}
